package com.paytm.pgplus.crypto.blockchain;

import com.paytm.pgplus.crypto.blockchain.blockActivity.BlockActs;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Data
@Slf4j
@Service
public class BlockChain {
    //  Blockchain: a public ledger of transactions.
    //    Implemented as a list of blocks - data sets of transactions
    //

    @Autowired
    private ServerProperties serverProperties;

    private ArrayList<Block>chain;
    public BlockChain(){
        chain=new ArrayList<>();

    }

    public void add_block(DataBlock data){
        Block last_Block=(chain.size()>0)?chain.get(chain.size()-1):null;

        chain.add(BlockActs.mine_block(last_Block,data));
        //System.out.println(String.valueOf(this));
    }
    public boolean is_valid_chain(ArrayList<Block> chain){
        //        Validate the incoming chain.
//        Enforce the following rules of the blockchain:
//          - the chain must start with the genesis block
//          - blocks must be formatted correctly
//        """
        if(!chain.get(0).getLast_hash().equals(Genesis.GENESIS_BLOCK_HASH)){
            new Throwable("1st block is not geneis");
        }

        int i=1;
        boolean ans=true;
        while(i<chain.size()){
            Block block=chain.get(i);
            Block last_block=chain.get(i-1);
            ans=ans|BlockActs.is_valid_block(last_block,block);
            i++;
        }
        return true;
    }
  public void  replace_chain(ArrayList<Block> InComingChain){
//         Replace the local chain with the incoming one if the following applies:
//          - The incoming chain is longer than the local one.
//          - The incoming chain is formatted properly.
//        """
      if(chain.size()>InComingChain.size()){
          new Throwable("incoming chain size must be larger");
      }
      try {
          is_valid_chain(InComingChain);
      }
      catch (Exception e){
          new Throwable("invalid chain is coming");

      }
      chain=InComingChain;
  }


}
