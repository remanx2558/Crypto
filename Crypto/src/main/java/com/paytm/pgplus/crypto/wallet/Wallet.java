package com.paytm.pgplus.crypto.wallet;

import com.google.gson.Gson;
import com.paytm.pgplus.crypto.Config;
import com.paytm.pgplus.crypto.blockchain.Block;
import com.paytm.pgplus.crypto.blockchain.BlockChain;
import com.paytm.pgplus.crypto.blockchain.DataBlock;
import com.paytm.pgplus.crypto.util.JsonHelper;
import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

@Data
public class Wallet {
    private BlockChain blockChain;
    private UUID address;
    private String privateKey = generatePrivateKey();
    private String publicKey = generatePublicKey();
    private int balance;
    private KeyPair pair;

//    public void serializePublicKey(){
//        publicKey = publicKey.getBytes(StandardCharsets.UTF_8)
//    }

    public Wallet() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
        keyPairGen.initialize(2048);
         pair = keyPairGen.generateKeyPair();
        PrivateKey privKey = pair.getPrivate();
        PublicKey pubKey = pair.getPublic();
        Base64.Encoder encoder = Base64.getEncoder();

        ////////////////////////
        privateKey=encoder.encodeToString(privKey.getEncoded());
        publicKey=encoder.encodeToString(pubKey.getEncoded());
        balance=Config.STARTING_BALANCE;
        address=UUID.randomUUID();

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

    public String sign(Object data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance("SHA256withDSA");
        sign.initSign(pair.getPrivate());
        String jsonData=new Gson().toJson(data);
        byte[] bytes= jsonData.getBytes(StandardCharsets.UTF_8);
                //JsonHelper.dataBlockTojsonString(data).getBytes(StandardCharsets.UTF_8);
        sign.update(bytes);
        byte []signature=sign.sign();
        return signature.toString();

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

    public static boolean verifySign(String publicKey, Object data, String signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException {
        Signature sign = Signature.getInstance("SHA256withDSA");

        //And to decode the private use PKCS8EncodedKeySpec
        Base64.Decoder decoder = Base64.getDecoder();
        decoder.decode(publicKey);
        byte[] publicBytes = decoder.decode(publicKey);//or use Base64.decodeBase64(publicKey)
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        ///////
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

//    public void serializePublicKey(){
//        this.publicKey = this.publicKey.
//    }
}
