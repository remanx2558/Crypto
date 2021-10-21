package com.paytm.pgplus.crypto.blockchain;

import lombok.Data;

@Data
public class Block {
    //Block: a unit of storage.
    //    Store transactions in a blockchain that supports a cryptocurrency.

   private DataBlock data;
   private String timeStamp;
  private   String last_hash;
  private   String hash;
    public static final Long GENESIS_BLOCK_PROOF = 100L;
    public static final String GENESIS_BLOCK_TIME_STAMP = "1";
    public static final String GENESIS_BLOCK_LAST_HASH = "last_hash";
    public static final String GENESIS_BLOCK_HASH = "hash";
    public static final DataBlock GENESIS_BLOCK_DATABLOCK= new DataBlock("Genesis");




    public Block(String timeStamp, String last_hash, String hash, DataBlock data){
        this.timeStamp=timeStamp;
        this.last_hash=last_hash;
        this.hash=hash;
        this.data=data;

       // System.out.println(String.valueOf(this));
    }

}
