package com.paytm.pgplus.crypto.blockchain;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Data;

import java.util.HashMap;

@Data
public class Block {
    //Block: a unit of storage.
    //    Store transactions in a blockchain that supports a cryptocurrency.

   private DataBlock data;
   private String timeStamp;
  private   String last_hash;
  private   String hash;
  private long nonce;
  private long difficulty;




    public Block(String timeStamp, String last_hash, String hash, DataBlock data,long difficulty,long nonce){
        this.timeStamp=timeStamp;
        this.last_hash=last_hash;
        this.hash=hash;
        this.data=data;
        this.difficulty=difficulty;
        this.nonce=nonce;

       // System.out.println(String.valueOf(this));
    }

}
