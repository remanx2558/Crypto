package com.paytm.pgplus.crypto;

import java.util.HashMap;

public class Config {
    public final Long NANOSECONDS = 1L;
    public final Long MICROSECONDS = 1000L;
    public final Long MILLISECONDS = 1000000L;
    public final Long SECONDS = 1000000000L;
    public final Long MINE_RATE = 4000000000L;
    public final Long STARTING_BALANCE = 1000L;
    public final Long MINING_REWARD = 50L;
    public final HashMap<String,Long> MINING_REWARD_INPUT = new HashMap<>();

    public Config() {
        MINING_REWARD_INPUT.put("address",100L);
    }
}
