package com.paytm.pgplus.crypto.wallet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.pgplus.crypto.constants.Config;
import com.paytm.pgplus.crypto.util.GeneralUrils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import static com.paytm.pgplus.crypto.constants.Config.MINING_REWARD;
import static com.paytm.pgplus.crypto.constants.Config.MINING_REWARD_INPUT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
public class Transaction {

    private static Logger LOGGER = Logger.getLogger(Transaction.class.getName());
    private int id;//** not mendataory can be generated//convert to string 0-8 UUID.randomUUID()
    private int amount;
    private HashMap<String,Integer> output;//** not mendataory can be generated
    private HashMap<String,String> input;//** not mendataory can be generated
    private Wallet senderWallet;
    private String recipient;

    public Transaction(HashMap<String, String> MINING_REWARD_INPUT, HashMap<String, Integer> output) {

        this.output=output;
        this.input=MINING_REWARD_INPUT;


    }
    public Transaction(Wallet senderWallet,String recipent,int amount) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException, NoSuchProviderException {
        LOGGER.info("inside method");
        this.senderWallet=senderWallet;
        this.amount=amount;
        this.recipient=recipent;

        this.output=createOutput(senderWallet,recipient,amount);
        this.input=createInput(senderWallet,this.output);
        Random rand = new Random();
        this.id=rand.nextInt(1000);

                //Integer.parseInt(UUID.randomUUID().toString().substring(0,5));
    }


    public  HashMap<String ,Integer> createOutput(Wallet senderWallet, String recipient, int amount){
      //  HashMap<String,Integer> output = new HashMap<>();

        try {
            //before condition reverse
            if(output==null){
                output=new HashMap<>();
            }
            if (amount < senderWallet.getBalance()) {
                output.put(recipient,amount);
                output.put(senderWallet.getAddress(),senderWallet.getBalance()-amount);
            }
        }catch (Exception e){
            LOGGER.info("Amount exceeds balance");
        }
        return output;
   }

   public static HashMap<String,String> createInput(Wallet senderWallet,HashMap<String,Integer> output) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException, NoSuchProviderException {

        HashMap<String,String> input = new HashMap<>();
        input.put("timestamp",String.valueOf(System.nanoTime()));
        input.put("amount",String.valueOf(senderWallet.getBalance()));
        input.put("address",senderWallet.getAddress());
        input.put("public_key",senderWallet.getPublicKey());
        input.put("signature",senderWallet.sign(output)); //need to understand

        return input;
   }

   public void update(Wallet senderWallet,String recipient,int amount) throws Exception {

        if(amount>this.output.get(senderWallet.getAddress())) throw new Exception("Amount exceeds balance");

        if (this.output.containsKey(recipient)){
            this.output.put(recipient,this.output.get(recipient)+amount);
        }else{
            this.output.put(recipient,amount);
        }
        int temp = this.output.get(senderWallet.getAddress())-amount;
        this.output.put(senderWallet.getAddress().toString(),temp);
        this.input = createInput(senderWallet,this.output);
   }

    public String toJson(Transaction transaction){
        ObjectMapper mapper = new ObjectMapper();
        String transactionJson = "";
        try {
             transactionJson = mapper.writeValueAsString(transaction);
//            System.out.println("ResultingJSONstring = " + json);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return transactionJson;
    }

    public static Transaction fromJson(String transactionJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Transaction transaction = mapper.readValue(transactionJson, Transaction.class);
        return transaction;
    }

    public static void isValidTransaction(Transaction transaction) throws Exception {

        if (transaction.input == MINING_REWARD_INPUT){
            //now transaction says its mining reward ...so output should have only 1 entry from  MINING_REWARD
            if(!((transaction.output.size()==1)&& (GeneralUrils.mapToListValue(transaction.output).get(0) ==MINING_REWARD)) )throw
                    new Exception("Invalid mining reward");
        }

        //sum of output values
        int output_total = sum(transaction.output);

        if (Integer.valueOf(transaction.input.get("amount"))!=output_total) throw
                new Exception("Invalid transaction output value");

       if(!(Wallet.verifySign(transaction.input.get("public_key"),transaction.output,transaction.input.get("signature")))) {
           throw new Exception("Invalid signature");  //need understand and uncomment the code
       }
    }

    private static int sum(HashMap<String, Integer> output) {
        int sum = 0;
        for(int value : output.values()){
            sum += output.get(value);
        }
        return  sum;
    }

     public static Transaction rewardTransaction(Wallet minorWallet){
        //Transaction transaction = new Transaction();
         HashMap<String,Integer> output = new HashMap<>();
         output.put(minorWallet.getAddress(),MINING_REWARD);
         Transaction transaction = new Transaction(MINING_REWARD_INPUT, output);
         System.out.println(output+"*********** "+MINING_REWARD_INPUT);

         return transaction;
     }

}
