package com.paytm.pgplus.crypto.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class CryptoHash2 {

      //sha-256 encryption in byte format
      public static byte[] cryptoHash(long timeStamp, String lastHash, ArrayList<Integer>data, int difficulty, int nonce) throws NoSuchAlgorithmException {
          String input = timeStamp + lastHash + data.toString() + difficulty + nonce;
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
