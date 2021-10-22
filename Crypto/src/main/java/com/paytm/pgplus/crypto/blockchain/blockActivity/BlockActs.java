package com.paytm.pgplus.crypto.blockchain.blockActivity;

import com.paytm.pgplus.crypto.Config;
import com.paytm.pgplus.crypto.blockchain.Block;
import com.paytm.pgplus.crypto.blockchain.DataBlock;
import com.paytm.pgplus.crypto.util.CryptoHash;
import com.paytm.pgplus.crypto.util.HexToBinary;
import java.sql.Timestamp;

public class BlockActs {
   static public Block mine_block(Block last_block, DataBlock data){
     //    Mine a block based on the given last_block and data, until a block hash
       //    is found that meets the leading 0's proof of work requirement.

       if(last_block==null){
           System.out.println("last block null received");
       }
       long timeStamp = System.nanoTime();
        String last_hash=(last_block!=null)?last_block.getHash():"genesis_hash";
        long difficulty= adjust_difficulty(last_block,timeStamp);
        long nonce=0;
        String hash= CryptoHash.hashString(last_hash+timeStamp+String.valueOf(data)+difficulty+nonce);

        while(!HexToBinary.hexToBinary(hash).substring(0,(int)difficulty).equalsIgnoreCase(getStringOfNChars((int)difficulty,'0'))){
            nonce++;
            timeStamp=System.nanoTime();
            difficulty= (int) adjust_difficulty(last_block,timeStamp);
            hash=CryptoHash.hashString(last_hash+timeStamp+String.valueOf(data)+difficulty+nonce);
        }

        return new Block(timeStamp,last_hash,hash,data,difficulty,nonce);
    }
    static public String getStringOfNChars(int n, char c){
       StringBuilder str=new StringBuilder("0");
       for(int i=2;i<=n;i++){
           str.append(c+"");
       }
       return String.valueOf(str);
    }
    public static long adjust_difficulty(Block lastBlock2, long newTimeStamp){
        //       //   """
//        //        Calculate the adjusted difficulty according to the MINE_RATE.
//        //        Increase the difficulty for quickly mined blocks.
//        //        Decrease the difficulty for slowly mined blocks.
//        //        """
       if(lastBlock2==null && GENESIS.GENESIS_DIFFICULTY-1>0){
           return GENESIS.GENESIS_DIFFICULTY-1;
       }
       else if(lastBlock2==null && Config.MINE_RATE-1<=0){
           return GENESIS.GENESIS_DIFFICULTY+1;
       }
        if((newTimeStamp - lastBlock2.getTimeStamp()) < new Config().MINE_RATE){
            return lastBlock2.getDifficulty() + 1;
        }
        if((lastBlock2.getDifficulty() - 1) > 0){
            return lastBlock2.getDifficulty() - 1;
        }
        return 1;
    }
    public static boolean is_valid_block(Block last_block,Block block){
//   """
//        Validate block by enforcing the following rules:
//          - the block must have the proper last_hash reference
//          - the block must meet the proof of work requirement
//          - the difficulty must only adjust by 1
//          - the block hash must be a valid combination of the block fields
//        """
        boolean ans=true;
        if(!block.getLast_hash().equals(last_block.getHash())){
            ans=false;
            new Throwable("last hash must be same");
        }
        if(!HexToBinary.hexToBinary(block.getHash()).substring(0,(int)block.getDifficulty()).equals(getStringOfNChars((int) block.getDifficulty(),'0'))){}

        if(Math.abs(last_block.getDifficulty()-block.getDifficulty())>1){
            ans=false;
            new Throwable("difficulty diff must be one");

        }

        String reconstructedHash=CryptoHash.hashString(block.getLast_hash()+block.getTimeStamp()+String.valueOf(block.getData())+block.getDifficulty()+block.getNonce());
        if(!reconstructedHash.equals(block.getHash())){
            ans=false;
            new Throwable("hash donot mathc");

        }
        return ans;
    }


  }
