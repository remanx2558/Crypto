package com.paytm.pgplus.crypto.blockchain;

import lombok.Data;

@Data
public class Block {
    //Block: a unit of storage.
    //    Store transactions in a blockchain that supports a cryptocurrency.

    int data;
    Block(int data){
        this.data=data;
    }
}
