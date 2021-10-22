package com.paytm.pgplus.crypto.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoHash {

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
