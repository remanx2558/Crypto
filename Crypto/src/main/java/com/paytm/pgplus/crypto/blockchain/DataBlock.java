package com.paytm.pgplus.crypto.blockchain;

import com.paytm.pgplus.crypto.wallet.Transaction;
import lombok.Data;

import java.util.ArrayList;

@Data
public class DataBlock {
    ArrayList<Transaction> transactionArrayList;
    public DataBlock(ArrayList<Transaction> data){
        if(data == null) {
            data = new ArrayList<Transaction>();
        }
        this.transactionArrayList =data;
    }


}
