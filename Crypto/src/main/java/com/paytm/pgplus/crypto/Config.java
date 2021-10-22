package com.paytm.pgplus.crypto;

public class Config {
    public final Long NANOSECONDS = 1L;
    public final Long MICROSECONDS = 1000 * NANOSECONDS;
    public final Long MILLISECONDS = 1000*MICROSECONDS;
    public final Long SECONDS = 1000*MILLISECONDS;
    public final Long MINE_RATE = 4*SECONDS;
    public final Long STARTING_BALANCE = 1000L;
    public final Long MINING_REWARD = 50L;
//    MINING_REWARD_INPUT = { 'address': '*--official-mining-reward--*' }
}
