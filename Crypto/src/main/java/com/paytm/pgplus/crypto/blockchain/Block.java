package com.paytm.pgplus.crypto.blockchain;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Data;

@Data
public class Block {
    //Block: a unit of storage.
    //    Store transactions in a blockchain that supports a cryptocurrency.

    int data;
    Block(int data){
        this.data=data;
    }
}
