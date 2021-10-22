package com.paytm.pgplus.crypto.util;

import com.paytm.pgplus.crypto.blockchain.DataBlock;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoHash {
    //Return a sha-256 hash of the given arguments.

    public static String hashString(String str){
        String sha256 = Hashing.sha256().hashString(String.valueOf(str), StandardCharsets.UTF_8).toString();
        return sha256;
    }
    public String hashObject(Object obj) throws JsonProcessingException {
        ObjectMapper mapper=new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        return Hashing.sha256().hashString(json, StandardCharsets.UTF_8).toString();
    }
    public static String hashListObject(List<Object> args) throws JsonProcessingException {
        ObjectMapper mapper=new ObjectMapper();
        StringBuilder joined_data= new StringBuilder("");
        for(Object obj:args){
            String json_data = mapper.writeValueAsString(obj);
            joined_data.append(json_data);
        }
        return Hashing.sha256().hashString(String.valueOf(joined_data), StandardCharsets.UTF_8).toString();
    }

      //sha-256 encryption in byte format
      public static byte[] cryptoHash(String input) throws NoSuchAlgorithmException {
          MessageDigest md = MessageDigest.getInstance("SHA-256");
          return md.digest(input.getBytes(StandardCharsets.UTF_8));
      }

      //it is converting string in hex format
      public static String hexToString(byte[] hash){

          BigInteger number = new BigInteger(1, hash);
          StringBuilder hexString = new StringBuilder(number.toString(16));

          while (hexString.length() < 32)
          {
              hexString.insert(0, '0');
          }
          return hexString.toString();
      }

}
