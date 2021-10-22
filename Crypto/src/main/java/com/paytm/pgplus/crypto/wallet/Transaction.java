package com.paytm.pgplus.crypto.wallet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.pgplus.crypto.Config;

import java.util.HashMap;
import java.util.logging.Logger;

public class Transaction {

    private static Logger LOGGER = Logger.getLogger(Transaction.class.getName());

    private int id;
    private int amount;
    private HashMap<String,Integer> output;
    private HashMap<String,String> input;
    private SenderWallet senderWallet;
    private String recipient;

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<String, String> getInput() {
        return input;
    }

    public void setInput(HashMap<String, String> input) {
        this.input = input;
    }

    public HashMap<String, Integer> getOutput() {
        return output;
    }

    public void setOutput(HashMap<String, Integer> output) {
        this.output = output;
    }

    public Transaction(int id, int amount, HashMap<String, Integer> output, HashMap<String, String> input, SenderWallet senderWallet, String recipient) {
        this.id = id;
        this.amount = amount;
        this.output = output;
        this.input = input;
        this.senderWallet = senderWallet;
        this.recipient = recipient;
    }

    public Transaction(HashMap<String, String> MINING_REWARD_INPUT, HashMap<String, Integer> output) {

    }

    public static HashMap<String ,Integer> createOutout(SenderWallet senderWallet,String recipient,int amount){
        HashMap<String,Integer> output = new HashMap<>();

        try {
            if (amount > senderWallet.getBalance()) {
                output.put(recipient,amount);
                output.put(senderWallet.getAddress().toString(),senderWallet.getBalance()-amount);
            }
        }catch (Exception e){
            LOGGER.info("Amount exceeds balance");
        }
        return output;
   }

   public static HashMap<String,String> createInput(SenderWallet senderWallet,HashMap<String,Integer> output){

        HashMap<String,String> input = new HashMap<>();
        input.put("timestamp",String.valueOf(System.nanoTime()));
        input.put("amount",String.valueOf(senderWallet.getBalance()));
        input.put("address",senderWallet.getAddress().toString());
        input.put("public_key",senderWallet.getPublicKey());
        input.put("signature",senderWallet.getSignature()); //need to understand

        return input;
   }

   public void update(SenderWallet senderWallet,String recipient,int amount) throws Exception {

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

    public void isValidTransaction(Transaction transaction) throws Exception {

        if (transaction.input == new Config().MINING_REWARD_INPUT){
            if(!transaction.output.containsValue(new Config().MINING_REWARD)) throw
                    new Exception("Invalid mining reward");
        }

        int output_total = sum(transaction.output);

        if (Integer.valueOf(transaction.input.get("amount"))!=output_total) throw
                new Exception("Invalid transaction output value");

//        if(!(Wallet.verify(transaction.input.get("public_key"),transaction.output,transaction.input.get("signature"))))
//            throw new Exception("Invalid signature");  //need understand and uncomment the code
    }

    private int sum(HashMap<String, Integer> output) {
        int sum = 0;
        for(int value : output.values()){
            sum += output.get(value);
        }
        return  sum;
    }

     public static Transaction rewardTransaction(SenderWallet minorWallet){
        //Transaction transaction = new Transaction();
         HashMap<String,Integer> output = new HashMap<>();
         output.put(String.valueOf(minorWallet.getAddress()), (new Config().MINING_REWARD));
         Transaction transaction = new Transaction(new Config().MINING_REWARD_INPUT, output);

         return transaction;
     }

}
