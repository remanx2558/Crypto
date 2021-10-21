package com.paytm.pgplus.crypto.util;

import com.paytm.pgplus.crypto.blockchain.DataBlock;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;

public class CryptoHash {
    //Return a sha-256 hash of the given arguments.

    public String hashString(String str){
        String sha256 = Hashing.sha256().hashString(String.valueOf(str), StandardCharsets.UTF_8).toString();
        return sha256;
    }
    public String hashObject(Object obj) throws JsonProcessingException {
        ObjectMapper mapper=new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        return Hashing.sha256().hashString(json, StandardCharsets.UTF_8).toString();
    }
}
