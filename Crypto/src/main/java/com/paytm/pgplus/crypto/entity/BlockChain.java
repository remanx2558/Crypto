package com.paytm.pgplus.crypto.entity;

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
    public void add_block(int data){
        chain.add(new Block(data));
    }
}
