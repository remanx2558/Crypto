package com.paytm.pgplus.crypto.blockchain;

import com.paytm.pgplus.crypto.blockchain.blockActivity.BlockActs;
import lombok.Data;

import java.util.ArrayList;

@Data
public class BlockChain {
    //  Blockchain: a public ledger of transactions.
    //    Implemented as a list of blocks - data sets of transactions


    ArrayList<Block>chain;
    public BlockChain(){
        chain=new ArrayList<>();
    }
    public void add_block(DataBlock data){
        Block last_Block=(chain.size()>0)?chain.get(chain.size()-1):null;

        chain.add(BlockActs.mine_block(last_Block,data));
        //System.out.println(String.valueOf(this));
    }
}
