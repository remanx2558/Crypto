package com.paytm.pgplus.crypto.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CryptoController.class);

    @GetMapping("/")
    public String routeDefault(){
        return  "Welcome to the Crypto World";
    }

    @GetMapping("/blockchain")
    public String routeBlockchain(){
        return "xyz";
        //return jsonify(blockchain.to_json());
    }

}
