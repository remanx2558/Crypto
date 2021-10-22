package com.paytm.pgplus.crypto;

import java.util.HashMap;

public class Config {
<<<<<<< HEAD
    public final Long NANOSECONDS = 1L;
    public final Long MICROSECONDS = 1000L;
    public final Long MILLISECONDS = 1000000L;
    public final Long SECONDS = 1000000000L;
    public final Long MINE_RATE = 4000000000L;
    public final int STARTING_BALANCE = 1000;
    public final int MINING_REWARD = 50;
    public final HashMap<String,String> MINING_REWARD_INPUT = new HashMap<>();
=======
    public static final Long NANOSECONDS = 1L;
    public static final Long MICROSECONDS = 1000 * NANOSECONDS;
    public static final Long MILLISECONDS = 1000*MICROSECONDS;
    public static final Long SECONDS = 1000*MILLISECONDS;
    public static final Long MINE_RATE = 4*SECONDS;
    public final Long STARTING_BALANCE = 1000L;
    public final Long MINING_REWARD = 50L;
    public final HashMap<String,Long> MINING_REWARD_INPUT = new HashMap<>();
>>>>>>> a9e509ec9940ca691ee8723671efb07a7a02e04c

    public Config() {
        MINING_REWARD_INPUT.put("address", String.valueOf(100L));
    }
}
