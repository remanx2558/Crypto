package com.paytm.pgplus.crypto.constants;

import com.paytm.pgplus.crypto.blockchain.Block;
import com.paytm.pgplus.crypto.blockchain.DataBlock;

import java.util.HashMap;
import java.util.Map;

public class Config {

    //part-1
    public static final Long NANOSECONDS = 1L;
    public static final Long MICROSECONDS = 1000 * NANOSECONDS;
    public static final Long MILLISECONDS = 1000*MICROSECONDS;
    public static final Long SECONDS = 1000*MILLISECONDS;
    public static final Long MINE_RATE = 4*SECONDS;
    public static final int STARTING_BALANCE = 1000;

    //part-2
    public static final Long GENESIS_BLOCK_PROOF = 100L;
    public static final String GENESIS_BLOCK_TIME_STAMP = "1";
    public static final String GENESIS_BLOCK_LAST_HASH = "last_hash";
    public static final String GENESIS_BLOCK_HASH = "hash";
    public static final DataBlock GENESIS_BLOCK_DATABLOCK= null;
    public static final long GENESIS_NONCE=3;
    public static final long GENESIS_DIFFICULTY=3;
    public static final Block GENESIS=new Block(Long.valueOf(GENESIS_BLOCK_TIME_STAMP),GENESIS_BLOCK_LAST_HASH,GENESIS_BLOCK_HASH,GENESIS_BLOCK_DATABLOCK,GENESIS_DIFFICULTY,GENESIS_NONCE);
    //part-3
    public static final int MINING_REWARD = 50;
    public static final String MINER_REWARD_SENDER="*--official-mining-reward--*";
    public static final HashMap<String, String> MINING_REWARD_INPUT = new HashMap<>();
    static {
        MINING_REWARD_INPUT.put("address", MINER_REWARD_SENDER);
    }
    public static final String PUBLIC_KEY_PUBNUB="pub-c-2e0eaf2c-c906-429b-a182-e35dd19051d2";
    public static final String SUBSCRIBE_KEY_PUBNUB="sub-c-25eb0ef4-2d94-11ec-9ccf-0aac42b27a06";
    public static final String BLOCK_CHANNEL="block_channel";
    public static final String TRANSACTION_CHANNEL="transaction_channel";
    public static final String PRIVATESTRING_KEY="private_key";
    public static final String PUBLICSTRING_KEY="public_key";




    public Config() {

    }
}
