package com.paytm.pgplus.crypto.pubnub;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.paytm.pgplus.crypto.blockchain.Block;
import com.paytm.pgplus.crypto.blockchain.BlockChain;
import com.paytm.pgplus.crypto.util.JsonHelper;
import com.paytm.pgplus.crypto.wallet.Transaction;
import com.paytm.pgplus.crypto.wallet.TransactionPool;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static com.paytm.pgplus.crypto.constants.Config.*;

@Service
public class pubnubApp {
    /*DOCUMENTATION:
       Handles the publish/subscribe layer of the application.
       Provides communication between the nodes of the blockchain network.*/

    //PUB NUB object not of class
    static  public PubNub pubnub;
    //channel name
//    static final String channelNameBlock = BLOCK_CHANNEL;
//    static final String channelNameTransaction = TRANSACTION_CHANNEL;
    String[] channelsArr={BLOCK_CHANNEL,TRANSACTION_CHANNEL};

    BlockChain blockChain;
    TransactionPool transactionPool;

    //@PostConstruct guarantees that this initialize() method will be invoked only once in the bean lifecycle
    @PostConstruct
    public void initialize() {
        blockChain=new BlockChain();
        transactionPool=new TransactionPool();

        String publicKey=PUBLIC_KEY_PUBNUB;
        String subscribeKey=SUBSCRIBE_KEY_PUBNUB;
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(subscribeKey);
        pnConfiguration.setPublishKey(publicKey);
        //pnConfiguration.setUuid("myUniqueUUID");
        ///pubnub server take 1 argument configuration
        pubnub = new PubNub(pnConfiguration);
        System.out.println("Setting pubsnb.............: " );
        SubscribeCallback listner=new SubscribeCallback() {

            @Override
            public void status(PubNub pubNub,  PNStatus status) {

                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                } else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                    // Connect event. You can do stuff like publish, and know you'll get it.
                    // Or just use the connected event to confirm you are subscribed for
                    // UI / internal notifications, etc
                    if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

                    }
                } else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {
                    // Happens as part of our regular operation. This event happens when
                    // radio / connectivity is lost then regained.
                } else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {
                    // Handle message decryption error. Probably client configured to
                    // encrypt messages and on live data feed it received plain text.
                } }

            @SneakyThrows
            @Override
            public void message( PubNub pubNub,  PNMessageResult message) {
                //this is kind of listner of listner : this code will be implemeted when you receive a message via channel
                //message==new block mined : so that block need to be validated so just see that new broadcasted block can be added in our local chain

                // Handle new message stored in message.message
                // message.getChannel()
                // message.getSubscription():Message has been received on channel stored in


                JsonElement receivedMessageObject = message.getMessage();
                System.out.println("Received message: " + message.getChannel()+" is "+receivedMessageObject.toString());
                if(message.getChannel().equals(BLOCK_CHANNEL)){
                    Block block=JsonHelper.jsonObjectToBlock(message.getMessage());
                    ArrayList<Block>potential_chain=new ArrayList<>( blockChain.getChain());
                    potential_chain.add(block);

                    try {
                        blockChain.replace_chain(potential_chain);//this line might throw exception
                        transactionPool.clearTransactionfromTransactionBasedonBlockChain(blockChain);
                        System.out.println("chain replaced success  size is "+blockChain.getChain().size());

                    }
                    catch (Exception e){
                        System.out.println("chain replaced failure");

                    }
                }
                else if(message.getChannel().equals(TRANSACTION_CHANNEL)){
                    Transaction transaction=JsonHelper.jsonObjectToTransaction(message.getMessage());
                    transactionPool.setTransaction(transaction);
                    System.out.println("trancsaction added in local pool "+transaction);

                }
            }

            @Override
            public void presence( PubNub pubNub,  PNPresenceEventResult pnPresenceEventResult) {
            }

            @Override
            public void signal( PubNub pubNub,  PNSignalResult pnSignalResult) {
            }

            @Override
            public void uuid(PubNub pubNub,  PNUUIDMetadataResult pnuuidMetadataResult) {
            }

            @Override
            public void channel( PubNub pubNub,  PNChannelMetadataResult pnChannelMetadataResult) {
            }

            @Override
            public void membership( PubNub pubNub,  PNMembershipResult pnMembershipResult) {
            }

            @Override
            public void messageAction( PubNub pubNub,  PNMessageActionResult pnMessageActionResult) {
            }

            @Override
            public void file( PubNub pubNub, PNFileEventResult pnFileEventResult) {
            }


        };

        //actual running of pubsub takes channel list and then execute it
//        pubnub.subscribe()
//                .channels(Collections.singletonList(channelNameBlock))
//                .execute();
        pubnub.subscribe().channels(Arrays.asList(channelsArr)).execute();


        pubnub.addListener(listner);

    }





    public pubnubApp(BlockChain blockChain,TransactionPool transactionPool){
        this.blockChain=blockChain;
        this.transactionPool=transactionPool;
    }

    public static void publish(JsonObject messagePass) throws PubNubException {
        //sending og mesage on pubnub
        //publish(): publish message to channel
        //message(): contains message to send can be anything
        //syc : send message
        // it will sleep the main thread for 1 sec
        // ,each time the for loop runs
        pubnub.publish().channel(BLOCK_CHANNEL).message(messagePass).sync();

    }
    public void broadcast_block(Block block) throws JsonProcessingException, JSONException, PubNubException {
        JSONObject mJSONObject = JsonHelper.blockTojsonObject(block);
        pubnub.publish().channel(BLOCK_CHANNEL).message(mJSONObject).sync();

    }
    public void broadcast_transaction(Transaction transaction) throws JsonProcessingException, JSONException, PubNubException {
        JSONObject mJSONObject = JsonHelper.ObjectTojsonObject(transaction);
        pubnub.publish().channel(TRANSACTION_CHANNEL).message(mJSONObject).sync();

    }

}
