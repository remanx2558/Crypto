package com.paytm.pgplus.crypto.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.paytm.pgplus.crypto.blockchain.Block;
import com.paytm.pgplus.crypto.pubnub.pubnubApp;
import com.paytm.pgplus.crypto.scripts.AverageBlockRate;
import com.paytm.pgplus.crypto.wallet.Transaction;
import com.paytm.pgplus.crypto.blockchain.BlockChain;
import com.paytm.pgplus.crypto.blockchain.DataBlock;
import com.paytm.pgplus.crypto.util.CryptoHash;
import com.paytm.pgplus.crypto.wallet.TransactionPool;
import com.paytm.pgplus.crypto.wallet.Wallet;
import com.pubnub.api.PubNubException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/")
public class CryptoController {




    @Autowired
    private Environment env;

    @Autowired
    private BlockChain blockChain;

    @Autowired
    private TransactionPool transactionPool;

    @Autowired
    private pubnubApp pubnubApp;

    @Autowired
    private Wallet wallet;

//    @PostConstruct
//    public void initialize() {
//        String port=env.getProperty("server.port");
//        System.out.println("port is "+port);
//        final String uri = "http://localhost:"+port+"/blockChain";
//        System.out.println("url is "+uri);
//
//        //bb is just to get class from ArrayList<Block>bb
//        ArrayList<Block>bb = new ArrayList<>();
//        RestTemplate restTemplate = new RestTemplate();
//        ArrayList<Block> result = restTemplate.getForObject(uri, bb.getClass());
//        try {
//            blockChain.replace_chain(result);
//            System.out.println("chain replaced success  size is "+blockChain.getChain().size());
//
//        }
//        catch (Exception e){
//            System.out.println("chain replaced failure");
//        }
//
//    }
@EventListener(ApplicationReadyEvent.class)
public void doSomethingAfterStartup() {
        String port=env.getProperty("server.port");
        System.out.println("port is "+port);
        final String uri = "http://localhost:"+port+"/blockChain";
        System.out.println("url is "+uri);

        //bb is just to get class from ArrayList<Block>bb
        ArrayList<Block>bb = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        ArrayList<Block> result = restTemplate.getForObject(uri, bb.getClass());
        try {
            blockChain.replace_chain(result);
            System.out.println("chain replaced success  size is "+blockChain.getChain().size());

        }
        catch (Exception e){
            System.out.println("chain replaced failure as incoiming chain is smaller");
        }
        }


    public CryptoController() {
    }

    @GetMapping("/")
    public String welcome(){
        return "welcome crypto app";
    }
    @GetMapping("/create")
    public void createBlockChain(){
        BlockChain blockChain=new BlockChain();
        blockChain.setChain(new ArrayList<>());
        //blockChain.add_block(new DataBlock("one"));
        //blockChain.add_block(new DataBlock("two"));

      //System.out.println(blockChain.getChain().get(0)+" "+blockChain.getChain().get(1));



    }
    @GetMapping("/hashCheck")
    public void hashCheck() throws JsonProcessingException {
        List<Object>lit=new ArrayList<>();
        lit.add(1);
        lit.add("str");
        lit.add(new ArrayList<Integer>());
        String str= CryptoHash.hashListObject(lit);
        System.out.println("hash for 1 +str+{} is : "+str);
    }

    @GetMapping("/average")
    public void average() throws JsonProcessingException {
        AverageBlockRate.average_Block_Rate();
    }
    @GetMapping("/blockChain")
    public ArrayList<Block> route_blockchain() throws JsonProcessingException {
//        blockChain.add_block(new DataBlock("one"));
//        blockChain.add_block(new DataBlock("two"));
        return blockChain.getChain();
    }


    //Mine a Block add it to local BlockChain ...then broadcast both block and blockChain
    @GetMapping("/blockChain/mine")
    public Block mine() throws JsonProcessingException, JSONException, PubNubException {
        ArrayList<Transaction>list_tras=new ArrayList<>();
        blockChain.add_block(new DataBlock(list_tras));

        Block block=blockChain.getChain().get(blockChain.getChain().size()-1);

        pubnubApp pubnubApp=new pubnubApp(blockChain,transactionPool);
        //acting as Publisher
       pubnubApp.broadcast_block(block);
        return block;
    }
    @GetMapping("/syn")
    public ArrayList<Block> syncron(){
        String port=env.getProperty("server.port");
        System.out.println("port is "+port);
        final String uri = "http://localhost:"+port+"/blockChain";
        System.out.println("url is "+uri);

        //bb is just to get class from ArrayList<Block>bb
        ArrayList<Block>bb = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        ArrayList<Block> result = restTemplate.getForObject(uri, bb.getClass());
        try {
            blockChain.replace_chain(result);
            System.out.println("chain replaced success  size is "+blockChain.getChain().size());
            return bb;
        }
        catch (Exception e){
            System.out.println("chain replaced failure");
        }
        System.out.println(result+"FUN YOU");
        return bb;


    }
    @RequestMapping(value="/wallet/transaction/test", method = RequestMethod.POST)
    public Transaction beach(@RequestBody(required = false) String recipent,  Integer amount) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, JSONException, JsonProcessingException, PubNubException, InvalidKeySpecException, NoSuchProviderException {
Transaction transaction_data=new Transaction(wallet,"foo",12);
        System.out.println("id "+transaction_data.getId());
        System.out.println("amount"+transaction_data.getAmount());
        System.out.println("output "+transaction_data.getOutput().toString());
        System.out.println("inout "+transaction_data.getInput().toString());
        System.out.println("wallet "+transaction_data.getSenderWallet().toString());

        //just pubnub object few things do not change in it as @post used in pubnub
        pubnubApp pubnubApp=new pubnubApp(blockChain,transactionPool);
        pubnubApp.broadcast_transaction(transaction_data);
        return transaction_data;

       // return new Gson().toJson(transaction_data);
        }


}
