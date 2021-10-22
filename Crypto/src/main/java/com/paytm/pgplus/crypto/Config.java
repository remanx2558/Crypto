package com.paytm.pgplus.crypto;

import java.util.HashMap;

public class Config {

    public static final Long NANOSECONDS = 1L;
    public static final Long MICROSECONDS = 1000 * NANOSECONDS;
    public static final Long MILLISECONDS = 1000*MICROSECONDS;
    public static final Long SECONDS = 1000*MILLISECONDS;
    public static final Long MINE_RATE = 4*SECONDS;
    public final int STARTING_BALANCE = 1000;
    public final int MINING_REWARD = 50;
    public final HashMap<String, String> MINING_REWARD_INPUT = new HashMap<String, String>();
    public Config() {
        MINING_REWARD_INPUT.put("address", String.valueOf(100L));
    }
}
