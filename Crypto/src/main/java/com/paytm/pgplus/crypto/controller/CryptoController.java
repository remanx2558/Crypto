package com.paytm.pgplus.crypto.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paytm.pgplus.crypto.blockchain.Block;
import com.paytm.pgplus.crypto.scripts.AverageBlockRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.paytm.pgplus.crypto.blockchain.BlockChain;
import com.paytm.pgplus.crypto.blockchain.DataBlock;
import com.paytm.pgplus.crypto.util.CryptoHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/")
public class CryptoController {
    @Autowired
    private BlockChain blockChain;

    @GetMapping("/create")
    public void createBlockChain(){
        BlockChain blockChain=new BlockChain();
        blockChain.add_block(new DataBlock("one"));
        blockChain.add_block(new DataBlock("two"));

      System.out.println(blockChain.getChain().get(0)+" "+blockChain.getChain().get(1));



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
        return blockChain.getChain();
    }
    @GetMapping("")
    public String welcome() throws JsonProcessingException {
return "welcome to block chain app";    }

}
