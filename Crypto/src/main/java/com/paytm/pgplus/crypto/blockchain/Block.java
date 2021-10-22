package com.paytm.pgplus.crypto.blockchain;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Data;

import java.util.HashMap;

@Data
public class Block {
    //Block: a unit of storage.
    //    Store transactions in a blockchain that supports a cryptocurrency.

   private DataBlock data;
   private long timeStamp;
  private   String last_hash;
  private   String hash;
  private long nonce;
  private long difficulty;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return timeStamp == block.timeStamp && nonce == block.nonce && difficulty == block.difficulty && Objects.equals(data, block.data) && Objects.equals(last_hash, block.last_hash) && Objects.equals(hash, block.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, timeStamp, last_hash, hash, nonce, difficulty);
    }

    public Block(long timeStamp, String last_hash, String hash, DataBlock data, long difficulty, long nonce){
        this.timeStamp=timeStamp;
        this.last_hash=last_hash;
        this.hash=hash;
        this.data=data;
        this.difficulty=difficulty;
        this.nonce=nonce;

       // System.out.println(String.valueOf(this));
    }

}
