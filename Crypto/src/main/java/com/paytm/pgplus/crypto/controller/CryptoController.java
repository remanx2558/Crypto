package com.paytm.pgplus.crypto.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.paytm.pgplus.crypto.blockchain.BlockChain;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoController {

    @GetMapping("/create")
    public void createBlockChain(){
        BlockChain blockChain=new BlockChain();
        blockChain.add_block(1);
        blockChain.add_block(2);
        System.out.println(blockChain.getChain().get(0)+" ");
    }

}
