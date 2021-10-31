package com.paytm.pgplus.crypto.wallet;

import com.google.gson.Gson;
import com.paytm.pgplus.crypto.constants.Config;
import com.paytm.pgplus.crypto.blockchain.Block;
import com.paytm.pgplus.crypto.blockchain.BlockChain;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

import static com.paytm.pgplus.crypto.constants.Config.PRIVATESTRING_KEY;
import static com.paytm.pgplus.crypto.constants.Config.PUBLICSTRING_KEY;

@Service
@Data
public class Wallet {

    @Autowired
    private BlockChain blockChain;
    private String address;
    private String privateKey;
    private String publicKey;
    private int balance;
    @PostConstruct
    public void initialize() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA", "SUN");
        //   SecureRandom random = SecureRandom.getInstance("DSA", "SUN");
        keyPairGen.initialize(1024);
        //, random);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        privateKey=generatePrivateKey(keyPair);
        publicKey=generatePublicKey(keyPair);
        balance=calculateBalance(blockChain,address);
                //Config.STARTING_BALANCE;
        address=UUID.randomUUID().toString();
        System.out.println("walllet created atleast");
    }


    public Wallet() throws NoSuchAlgorithmException, NoSuchProviderException {


    }


    public String generatePrivateKey(KeyPair keyPair) throws NoSuchAlgorithmException, NoSuchProviderException {
        PrivateKey privateKey = keyPair.getPrivate();
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(privateKey.getEncoded());
    }


    public String generatePublicKey(KeyPair keyPair) throws NoSuchAlgorithmException, NoSuchProviderException {
        PublicKey publicKey = keyPair.getPublic();
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(publicKey.getEncoded());
    }
    public static Object stringToKey(String key, String type) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] keyBytes = decoder.decode(key);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");

        if(type.equalsIgnoreCase(PRIVATESTRING_KEY)){
            PKCS8EncodedKeySpec keySpec=new PKCS8EncodedKeySpec(keyBytes);
            PrivateKey privKey = keyFactory.generatePrivate(keySpec);
            return privKey;
        }
        else if(type.equalsIgnoreCase(PUBLICSTRING_KEY)){
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            return pubKey;
        }
        return null;
    }
    public String sign(Object data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, NoSuchProviderException {
        Signature sign = Signature.getInstance("SHA256withDSA");
        PrivateKey privKey = (PrivateKey) stringToKey(privateKey,PRIVATESTRING_KEY);
        sign.initSign(privKey);

        String jsonData=new Gson().toJson(data);
        byte[] bytes= jsonData.getBytes(StandardCharsets.UTF_8);
                //JsonHelper.dataBlockTojsonString(data).getBytes(StandardCharsets.UTF_8);
        sign.update(bytes);
        byte []signature=sign.sign();
        return signature.toString();

    }
    public static boolean verifySign(String publicKey, Object data, String signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, NoSuchProviderException {
        Signature sign = Signature.getInstance("SHA256withDSA");
        PublicKey pubKey = (PublicKey) stringToKey(publicKey,PUBLICSTRING_KEY);
        sign.initVerify(pubKey);

        String jsonData=new Gson().toJson(data);

        byte[] bytes= jsonData.getBytes(StandardCharsets.UTF_8);
        //JsonHelper.dataBlockTojsonString(data).getBytes(StandardCharsets.UTF_8);
        sign.update(bytes);
        boolean bool = sign.verify(signature.getBytes(StandardCharsets.UTF_8));
        if(bool) {
            System.out.println("Signature verified");
        } else {
            System.out.println("Signature failed");
        }
        return bool;
    }

    public static int calculateBalance(BlockChain blockChain, String address){
        int balance = new Config().STARTING_BALANCE;
        if (blockChain == null) return balance;
        //int n = blockChain.getSize();
        int n = 4;
        for(Block block : blockChain.getChain()){
            for(Transaction transaction : block.getTransactions().getTransactionArrayList()){
                if(transaction.getInput().get("address") == address.toString()){
                    //any time address conduct a new transaction it resets the balance
                    balance = transaction.getOutput().get(address);
                }
                else if(transaction.getOutput().containsKey(address)){
                    balance+=transaction.getOutput().get(address);
                }
            }
        }

        return balance;
    }



    ///wrong Implementation of it
    public String serializePublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Base64.Decoder decoder = Base64.getDecoder();

        byte[] publicBytes = decoder.decode(publicKey);//or use Base64.decodeBase64(publicKey)
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        return pubKey.toString();
    }
}
