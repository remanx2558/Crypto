package com.paytm.pgplus.crypto.blockchain.blockActivity;

import com.paytm.pgplus.crypto.blockchain.Block;
import com.paytm.pgplus.crypto.blockchain.DataBlock;
import com.paytm.pgplus.crypto.util.CryptoHash;

import java.sql.Timestamp;

public class BlockActs {
   static public Block mine_block(Block last_block, DataBlock data){
     //    Mine a block based on the given last_block and data, until a block hash
       //    is found that meets the leading 0's proof of work requirement.

       if(last_block==null){
           System.out.println("last block null received");
       }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String timeStamp=String.valueOf(timestamp);
        String last_hash=(last_block!=null)?last_block.getHash():"genesis_hash";
        int difficulty= (last_block!=null)?(int) last_block.getDifficulty():(int)(GENESIS.GENESIS_DIFFICULTY);
        long nonce=0;
        String hash= CryptoHash.hashString(last_hash+timeStamp+String.valueOf(data)+difficulty+nonce);

        while(!hash.substring(0,difficulty).equalsIgnoreCase(getStringOfNChars(difficulty,'0'))){
            nonce++;
            timeStamp=String.valueOf(new Timestamp(System.currentTimeMillis()));
            hash=CryptoHash.hashString(last_hash+timeStamp+String.valueOf(data)+difficulty+nonce);
        }

        return new Block(timeStamp,last_hash,hash,data,difficulty,nonce);
    }
    static public String getStringOfNChars(int n, char c){
       StringBuilder str=new StringBuilder("");
       for(int i=0;i<n;i++){
           str.append(c+"");
       }
       return String.valueOf(str);
    }
    static public void adjust_difficulty(Block last_block,String new_timeStamp){
       //   """
        //        Calculate the adjusted difficulty according to the MINE_RATE.
        //        Increase the difficulty for quickly mined blocks.
        //        Decrease the difficulty for slowly mined blocks.
        //        """

    }
  }
