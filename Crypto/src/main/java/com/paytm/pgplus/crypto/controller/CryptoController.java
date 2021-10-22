package com.paytm.pgplus.crypto.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paytm.pgplus.crypto.blockchain.Block;
import com.paytm.pgplus.crypto.pubnub.pubnubApp;
import com.paytm.pgplus.crypto.scripts.AverageBlockRate;
import com.paytm.pgplus.crypto.wallet.Transaction;
import com.paytm.pgplus.crypto.blockchain.BlockChain;
import com.paytm.pgplus.crypto.blockchain.DataBlock;
import com.paytm.pgplus.crypto.util.CryptoHash;
import com.pubnub.api.PubNubException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/")
public class CryptoController {
    @Autowired
    private BlockChain blockChain;

//    @Autowired
//    private pubnubApp pubnubApp;

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
    @GetMapping("")
    public String welcome() throws JsonProcessingException {
return "welcome to block chain app";    }

    @GetMapping("/blockChain/mine")
    public Block mine() throws JsonProcessingException, JSONException, PubNubException {
        ArrayList<Transaction>list_tras=new ArrayList<>();
        blockChain.add_block(new DataBlock(list_tras));

        Block block=blockChain.getChain().get(blockChain.getChain().size()-1);

        //pubnubApp pubnubApp=new pubnubApp(blockChain);
     //  pubnubApp.broadcast_block(block);
        return block;
    }
    @GetMapping("/syn")
    public ArrayList<Block> syncron(){
        final String uri = "http://localhost:8081/blockChain";

        ArrayList<Block>bb = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        ArrayList<Block> result = restTemplate.getForObject(uri, bb.getClass());
        try {
            blockChain.replace_chain(result);
            System.out.println("chain replaced success  size is "+blockChain.getChain().size());
        }
        catch (Exception e){
            System.out.println("chain replaced failure");
        }



        System.out.println(result);
        return result;
    }


}
