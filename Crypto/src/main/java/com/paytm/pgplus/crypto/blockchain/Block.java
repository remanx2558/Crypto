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

import static com.paytm.pgplus.crypto.util.CryptoHash.cryptoHash;
import static com.paytm.pgplus.crypto.util.CryptoHash.hexToString;
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
public class Block {



    private static Logger logger = Logger.getLogger(Block.class.getName());



    private String hash = "last_hash";
    private String lastHash = "genesis_last_hash";
    private ArrayList<Integer> data;

    private long timeStamp = 1;
    private int nonce = 0;
    private int difficulty = 3;



    public void toJson(Block block){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(block);
            System.out.println("ResultingJSONstring = " + json);
            //System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Block mineBlock(Block lastBlock, ArrayList<Integer> data) throws NoSuchAlgorithmException {
        Block block = new Block();
        block.timeStamp = System.nanoTime();
        block.lastHash = lastBlock.hash;
        block.difficulty = Block.adjust_difficulty(lastBlock, block.timeStamp);
        block.nonce = 0;

        block.hash = hexToString(cryptoHash(timeStamp, lastHash, data, difficulty, nonce));

        //String difficultyTemp =
        while (hexToBinary(hash).substring(0, difficulty) != getString(difficulty)){
            nonce += 1;
            timeStamp = System.nanoTime();
            difficulty = Block.adjust_difficulty(lastBlock, timeStamp);
            hash = hexToString(cryptoHash(timeStamp, lastHash, data, difficulty, nonce));
        }

        return block;
    }

    public String getString (int difficulty){
        StringBuffer str = new StringBuffer();
        for(int i=0; i<difficulty; i++){
            str.append("0");
        }
        return str.toString();
    }

    public static int adjust_difficulty(Block lastBlock, long newTimeStamp){
        if((newTimeStamp - lastBlock.timeStamp) < new Config().MINE_RATE){
            return lastBlock.difficulty + 1;
        }
        if((lastBlock.difficulty - 1) > 0){
            return lastBlock.difficulty - 1;
        }
        return 1;
    }

    public void isValidBlock(Block lastBlock, Block block) throws Exception {
        if (block.lastHash != lastBlock.hash) throw
                new Exception("The block last_hash must be correct");

        if (hexToBinary(block.hash).substring(0, block.difficulty) != getString(difficulty)) throw
                new Exception("The proof of work requirement was not met");

        if ((Math.abs(lastBlock.difficulty - block.difficulty)) > 1) throw
                new Exception ("The block difficulty must only adjust by 1");


        String reconstructedHash = hexToString(cryptoHash(
                block.timeStamp,
                block.lastHash,
                block.data,
                block.nonce,
                block.difficulty
        ));

        if (block.hash != reconstructedHash) throw
            new Exception("The block hash must be correct");
    }


}