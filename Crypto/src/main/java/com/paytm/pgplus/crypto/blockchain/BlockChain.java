package com.paytm.pgplus.crypto.blockchain;

import com.paytm.pgplus.crypto.blockchain.blockActivity.BlockActs;
import com.paytm.pgplus.crypto.wallet.Transaction;
import com.paytm.pgplus.crypto.wallet.Wallet;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.paytm.pgplus.crypto.constants.Config.MINING_REWARD_INPUT;

@Data
@Slf4j
@Service
public class BlockChain {
    //  Blockchain: a public ledger of transactions.
    //    Implemented as a list of blocks - data sets of transactions
    //

    @Autowired
    private ServerProperties serverProperties;

    private ArrayList<Block>chain=new ArrayList<>();
    public BlockChain(){
       // chain=new ArrayList<>();

    }
    public BlockChain(ArrayList<Block>Inputchain){
        chain=Inputchain;
    }

    public void add_block(DataBlock data){
        Block last_Block=(chain.size()>0)?chain.get(chain.size()-1):null;

        chain.add(BlockActs.mine_block(last_Block,data));
        //System.out.println(String.valueOf(this));
    }
    public void is_valid_chain(ArrayList<Block> chain) throws Exception {
        //        Validate the incoming chain.
//        Enforce the following rules of the blockchain:
//          - the chain must start with the genesis block
//          - blocks must be formatted correctly
//        """
        if(!chain.get(0).getLast_hash().equals(Genesis.GENESIS_BLOCK_HASH)){
            new Throwable("1st block is not geneis");
        }

        int i=1;
        while(i<chain.size()){
            Block block=chain.get(i);
            Block last_block=chain.get(i-1);
            BlockActs.is_valid_block(last_block,block);
            i++;
        }
        is_valid_transaction_chain(chain);

    }
    public void is_valid_transaction_chain(ArrayList<Block> chain) throws Exception {
       //check formating of transaction within blocks

        //rule1:each transaction must be unique in chain
       //rule2:only 1 mining reward per block
       //rule3:each transaction must be valid
        Set<Integer> transaction_ids = new HashSet<Integer>();
        for(int i=0;i<chain.size();i++){
            Block block=chain.get(i);
            boolean hasMiningReward=false;
            for(Transaction transaction:block.getTransactions().transactionArrayList){
                int tid=transaction.getId();

                if(transaction_ids.contains(tid)){
                    new Throwable("transaction id "+tid+"found dublicate");
                }
                transaction_ids.add(tid);
                ////
                if(transaction.getInput().equals(MINING_REWARD_INPUT)){
                    if(hasMiningReward){
                        new Throwable("more then once mining reward is provided \n " +
                                "check block with hash :"+block.getHash());
                    }
                    hasMiningReward=true;
                }
                else{

                    ArrayList<Block>historic_chain=new ArrayList<>(chain.subList(0,i));
                    BlockChain historicBlockChain=new BlockChain(historic_chain);
                    int historic_balance= Wallet.calculateBalance(historicBlockChain,transaction.getInput().get("address"));
                    if(historic_balance!=Integer.parseInt(transaction.getInput().get("amount"))){
                        new Throwable("transaction has an invalid input amount \n"+"for transaction id "+tid);
                    }
                }

                Transaction.isValidTransaction(transaction);

            }
        }


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
