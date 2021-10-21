package com.paytm.pgplus.crypto.controller;

import com.paytm.pgplus.crypto.blockchain.BlockChain;
import com.paytm.pgplus.crypto.blockchain.DataBlock;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoController {

    @GetMapping("/create")
    public void createBlockChain(){
        BlockChain blockChain=new BlockChain();
        blockChain.add_block(new DataBlock("one"));
        blockChain.add_block(new DataBlock("two"));

      System.out.println(blockChain.getChain().get(0)+" "+blockChain.getChain().get(1));

    }

}
