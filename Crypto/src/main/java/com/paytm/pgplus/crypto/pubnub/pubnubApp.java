package com.paytm.pgplus.crypto.pubnub;
import java.util.Collections;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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


public class pubnubApp {
    public static void main(String[] args) throws PubNubException, InterruptedException {
        String publicKey="pub-c-2e0eaf2c-c906-429b-a182-e35dd19051d2";
        String subscribeKey="sub-c-25eb0ef4-2d94-11ec-9ccf-0aac42b27a06";
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(subscribeKey);
        pnConfiguration.setPublishKey(publicKey);
        //pnConfiguration.setUuid("myUniqueUUID");

        ///pubnub server take 1 argument configuration
        PubNub pubnub = new PubNub(pnConfiguration);

        //channel name
        final String channelName = "TestChannel";
        // create message payload using Gson
        final JsonObject messageJsonObject = new JsonObject();
        messageJsonObject.addProperty("msg", "Hello World");

        System.out.println("Message to send: " + messageJsonObject.toString());

        //actual running of pubsub takes channel list and then execute it
        pubnub.subscribe()
                .channels(Collections.singletonList(channelName))
                .execute();

        //Add event listeners
        SubscribeCallback listner=new SubscribeCallback() {
            @Override
            public void status(PubNub pubNub,  PNStatus status) {

                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    // This event happens when radio / connectivity is lost
                } else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                    // Connect event. You can do stuff like publish, and know you'll get it.
                    // Or just use the connected event to confirm you are subscribed for
                    // UI / internal notifications, etc
                    if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                        pubnub.publish()
                                .channel(channelName)
                                .message(messageJsonObject)
                                .async((result, publishStatus) -> {
                                    if (!publishStatus.isError()) {
                                        // Message successfully published to specified channel.
                                    }
                                    // Request processing failed.
                                    else {
                                        // Handle message publish error
                                        // Check 'category' property to find out
                                        // issues because of which the request failed.
                                        // Request can be resent using: [status retry];
                                    }
                                });
                    }
                } else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {
                    // Happens as part of our regular operation. This event happens when
                    // radio / connectivity is lost then regained.
                } else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {
                    // Handle message decryption error. Probably client configured to
                    // encrypt messages and on live data feed it received plain text.
                } }

            @Override
            public void message( PubNub pubNub,  PNMessageResult message) {
                // Handle new message stored in message.message
                if (message.getChannel() != null) {
                    // Message has been received on channel group stored in
                    // message.getChannel()
                } else {
                    // Message has been received on channel stored in
                    // message.getSubscription()
                }

                JsonElement receivedMessageObject = message.getMessage();
                System.out.println("Received message: " + receivedMessageObject.toString());
                // extract desired parts of the payload, using Gson
                String msg = message.getMessage().getAsJsonObject().get("msg").getAsString();
                System.out.println("The content of the message is: " + msg);

                // log the following items with your favorite logger - message.getMessage() -
                // message.getSubscription() - message.getTimetoken()

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
        pubnub.addListener(listner);












        //sending og mesage on pubnub
        //publish(): publish message to channel
        //message(): contains message to send can be anything
        //syc : send message
        // it will sleep the main thread for 1 sec
        // ,each time the for loop runs

        pubnub.publish().channel(channelName).message(messageJsonObject).sync();




    }
}
