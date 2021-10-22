package com.paytm.pgplus.crypto.blockchain;

import com.paytm.pgplus.crypto.blockchain.blockActivity.BlockActs;
import lombok.Data;

import java.util.ArrayList;

@Data
public class BlockChain {
    //  Blockchain: a public ledger of transactions.
    //    Implemented as a list of blocks - data sets of transactions
    //
    ArrayList<Block>chain;
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
}
