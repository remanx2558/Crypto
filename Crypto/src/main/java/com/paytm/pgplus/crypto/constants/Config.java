package com.paytm.pgplus.crypto.constants;

import java.util.HashMap;

public class Config {

    public static final Long NANOSECONDS = 1L;
    public static final Long MICROSECONDS = 1000 * NANOSECONDS;
    public static final Long MILLISECONDS = 1000*MICROSECONDS;
    public static final Long SECONDS = 1000*MILLISECONDS;
    public static final Long MINE_RATE = 4*SECONDS;
    public static final int STARTING_BALANCE = 1000;
    public final int MINING_REWARD = 50;
    public final HashMap<String, String> MINING_REWARD_INPUT = new HashMap<String, String>();
    public static final String PUBLIC_KEY_PUBNUB="pub-c-2e0eaf2c-c906-429b-a182-e35dd19051d2";
    public static final String SUBSCRIBE_KEY_PUBNUB="sub-c-25eb0ef4-2d94-11ec-9ccf-0aac42b27a06";
    public static final String BLOCK_CHANNEL="block_channel";
    public static final String TRANSACTION_CHANNEL="transaction_channel";
    public static final String PRIVATESTRING_KEY="private_key";
    public static final String PUBLICSTRING_KEY="public_key";




    public Config() {
        MINING_REWARD_INPUT.put("address", String.valueOf(100L));
    }
}
