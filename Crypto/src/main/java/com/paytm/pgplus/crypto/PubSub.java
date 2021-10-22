package com.paytm.pgplus.crypto;


import com.paytm.pgplus.crypto.blockchain.BlockChain;
import com.paytm.pgplus.crypto.wallet.TransactionPool;

class Listener{
    private BlockChain blockChain;
    private TransactionPool transactionPool;

    public Listener(BlockChain blockChain, TransactionPool transactionPool) {
        this.blockChain = blockChain;
        this.transactionPool = transactionPool;
    }

    public static void message(){

    }

}

public class PubSub {

}
