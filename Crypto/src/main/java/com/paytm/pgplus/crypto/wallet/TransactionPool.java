package com.paytm.pgplus.crypto.wallet;

import com.paytm.pgplus.crypto.blockchain.Block;
import com.paytm.pgplus.crypto.blockchain.BlockChain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.logging.Logger;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class TransactionPool {
    private static Logger LOGGER = Logger.getLogger(TransactionPool.class.getName());

    //Transaction Id: Transactions
    private HashMap<Integer, Transaction> transactionMap=new HashMap<>();
    public void TransactionPool(){
        transactionMap=new HashMap<>();
    }


    public void setTransaction(Transaction transaction){
        transactionMap.put(transaction.getId(), transaction);
    }

    public Transaction existingTransaction(String address){
        for(Transaction transaction : transactionMap.values()){
            if(transaction.getInput().get(address) == address) return transaction;
        }
        return null;
    }

    public String transactionData(Transaction transaction){
        String toJson = transaction.toJson(transaction);
        String result = "";
        for (Map.Entry mapElement : transactionMap.entrySet()) {
            Integer key = (Integer)mapElement.getKey();

            // Add some bonus marks
            // to all the students and print it
            Transaction value = (Transaction) mapElement.getValue();
            result += value.toJson(transaction);
        }
        return result;
    }

    public void blockChainTransaction(BlockChain blockChain) throws Exception {
        for (Block block: blockChain.getChain()){
            for(Transaction transaction : block.getData().getData()){
                String s = "key error";
                try{
                    transactionMap.remove(transaction.getId());
                }catch (Exception e){
                    LOGGER.info("key Error");
                }

            }
        }
    }

}
