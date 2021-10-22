package com.paytm.pgplus.crypto.blockchain;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.paytm.pgplus.crypto.util.CryptoHash2.cryptoHash;
import static com.paytm.pgplus.crypto.util.CryptoHash2.hexToString;
import static com.paytm.pgplus.crypto.util.HexToBinary.hexToBinary;

import com.paytm.pgplus.crypto.Config;
/**
 * This class reprsents a Block in the Blockchain. It is the most basic building
 * block of the Blockchain.
 *
 * @author Praveendra Singh
 *
 */
@Data
@Builder
/**
 * Hash should be calculated on the ordered list of attributes and hence keeping
 * them sorted to ensure that hashing is consistent.
 */
@JsonPropertyOrder(alphabetic = true)
@AllArgsConstructor
@NoArgsConstructor
public class Block2 {



    private static Logger logger = Logger.getLogger(Block2.class.getName());



    private String hash = "last_hash";
    private String lastHash = "genesis_last_hash";
    private ArrayList<Integer> data;

    private long timeStamp = 1;
    private int nonce = 0;
    private int difficulty = 3;



    public void toJson(Block2 block2){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(block2);
            System.out.println("ResultingJSONstring = " + json);
            //System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Block2 mineBlock(Block2 lastBlock2, ArrayList<Integer> data) throws NoSuchAlgorithmException {
        Block2 block2 = new Block2();
        block2.timeStamp = System.nanoTime();
        block2.lastHash = lastBlock2.hash;
        block2.difficulty = Block2.adjust_difficulty(lastBlock2, block2.timeStamp);
        block2.nonce = 0;

        block2.hash = hexToString(cryptoHash(timeStamp, lastHash, data, difficulty, nonce));

        //String difficultyTemp =
        while (hexToBinary(hash).substring(0, difficulty) != getString(difficulty)){
            nonce += 1;
            timeStamp = System.nanoTime();
            difficulty = Block2.adjust_difficulty(lastBlock2, timeStamp);
            hash = hexToString(cryptoHash(timeStamp, lastHash, data, difficulty, nonce));
        }

        return block2;
    }

    public String getString (int difficulty){
        StringBuffer str = new StringBuffer();
        for(int i=0; i<difficulty; i++){
            str.append("0");
        }
        return str.toString();
    }

    public static int adjust_difficulty(Block2 lastBlock2, long newTimeStamp){
        if((newTimeStamp - lastBlock2.timeStamp) < new Config().MINE_RATE){
            return lastBlock2.difficulty + 1;
        }
        if((lastBlock2.difficulty - 1) > 0){
            return lastBlock2.difficulty - 1;
        }
        return 1;
    }

    public void isValidBlock(Block2 lastBlock2, Block2 block2) throws Exception {
        if (block2.lastHash != lastBlock2.hash) throw
                new Exception("The block last_hash must be correct");

        if (hexToBinary(block2.hash).substring(0, block2.difficulty) != getString(difficulty)) throw
                new Exception("The proof of work requirement was not met");

        if ((Math.abs(lastBlock2.difficulty - block2.difficulty)) > 1) throw
                new Exception ("The block difficulty must only adjust by 1");


        String reconstructedHash = hexToString(cryptoHash(
                block2.timeStamp,
                block2.lastHash,
                block2.data,
                block2.nonce,
                block2.difficulty
        ));

        if (block2.hash != reconstructedHash) throw
            new Exception("The block hash must be correct");
    }


}