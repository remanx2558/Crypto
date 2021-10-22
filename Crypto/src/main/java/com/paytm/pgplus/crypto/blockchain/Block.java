package com.paytm.pgplus.crypto.blockchain;

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




    public Block(String timeStamp, String last_hash, String hash, DataBlock data){
        this.timeStamp=timeStamp;
        this.last_hash=last_hash;
        this.hash=hash;
        this.data=data;

       // System.out.println(String.valueOf(this));
    }

}
