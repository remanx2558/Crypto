package com.paytm.pgplus.crypto.wallet;

import com.paytm.pgplus.crypto.Config;
import com.paytm.pgplus.crypto.blockchain.Block;
import com.paytm.pgplus.crypto.blockchain.BlockChain;
import com.paytm.pgplus.crypto.blockchain.DataBlock;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.UUID;

public class Wallet {
    private BlockChain blockChain;
    private UUID address;
    private String privateKey = generatePrivateKey();
    private String publicKey = generatePublicKey();

//    public void serializePublicKey(){
//        publicKey = publicKey.getBytes(StandardCharsets.UTF_8)
//    }

    public Wallet() throws NoSuchAlgorithmException, NoSuchProviderException {
    }


    public String generatePrivateKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");

        // Initialize KeyPairGenerator.
        SecureRandom random = SecureRandom.getInstance("SECP256k1", "SUN");
        keyGen.initialize(1024, random);

        // Generate Key Pairs, a private key and a public key.
        KeyPair keyPair = keyGen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        Base64.Encoder encoder = Base64.getEncoder();
        //System.out.println("privateKey: " + encoder.encodeToString(privateKey.getEncoded()));
        //System.out.println("publicKey: " + encoder.encodeToString(publicKey.getEncoded()));
        return encoder.encodeToString(privateKey.getEncoded());
    }


    public String generatePublicKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");

        // Initialize KeyPairGenerator.
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);

        // Generate Key Pairs, a private key and a public key.
        KeyPair keyPair = keyGen.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();

        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(publicKey.getEncoded());
    }

    public void sign(DataBlock data){

    }

    public int calculateBalance(BlockChain blockChain, UUID address){
        int balance = new Config().STARTING_BALANCE;
        if (blockChain == null) return balance;
        //int n = blockChain.getSize();
        int n = 4;
        for(Block block : blockChain.getChain()){
            for(Transaction transaction : block.getData().getData()){
                if(transaction.getInput().get("address") == address.toString()){
                    balance = transaction.getOutput().get(address);
                }
            }
        }

        return balance;
    }

    public void verify(String publicKey, DataBlock data, String signature){

    }

//    public void serializePublicKey(){
//        this.publicKey = this.publicKey.
//    }
}
