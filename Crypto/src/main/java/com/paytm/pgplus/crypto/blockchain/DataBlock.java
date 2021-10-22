package com.paytm.pgplus.crypto.blockchain;

import lombok.Data;

@Data
public class DataBlock {
    String data;
    public DataBlock(String data){
        this.data=data;
    }

}
