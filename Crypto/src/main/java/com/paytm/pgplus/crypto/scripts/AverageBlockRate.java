package com.paytm.pgplus.crypto.scripts;

import com.paytm.pgplus.crypto.Config;
import com.paytm.pgplus.crypto.blockchain.BlockChain;
import com.paytm.pgplus.crypto.blockchain.DataBlock;
import org.joda.time.Seconds;

import java.util.ArrayList;

public class AverageBlockRate {
    static ArrayList<Long> times;
    public static void average_Block_Rate(){
        times=new ArrayList<>();
        long sum=0;

        BlockChain blockChain=new BlockChain();
        for(int i=0;i<1000;i++){
            long starttime=System.nanoTime();
            //blockChain.add_block(new DataBlock(""+i));
            long endtime=System.nanoTime();
            long timetomine=(endtime-starttime)/ Config.SECONDS;
            times.add(timetomine);
            sum=sum+timetomine;
            long average_Time=sum/times.size();
            System.out.println("averge time"+average_Time);


        }

    }


}
