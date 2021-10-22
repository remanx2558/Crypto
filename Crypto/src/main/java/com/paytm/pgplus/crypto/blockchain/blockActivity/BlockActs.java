package com.paytm.pgplus.crypto.blockchain.blockActivity;

import com.paytm.pgplus.crypto.blockchain.Block;
import com.paytm.pgplus.crypto.blockchain.DataBlock;
import com.paytm.pgplus.crypto.util.CryptoHash;

import java.sql.Timestamp;

public class BlockActs {
   static public Block mine_block(Block last_block, DataBlock data){
       if(last_block==null){
           System.out.println("last block null received");
       }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String timeStamp=String.valueOf(timestamp);
        String last_hash=(last_block!=null)?last_block.getHash():"genesis_hash";
        String hash= CryptoHash.hashString(last_hash+timeStamp+String.valueOf(data));
        return new Block(timeStamp,last_hash,hash,data);
    }
  }
