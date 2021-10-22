package com.paytm.pgplus.crypto.util;

public class HexToBinary{

    public static String hexToBinary(String hexadecimal) {

        int i;
        char ch;
        String binary = "";
        int returnedBinary;
        hexadecimal = hexadecimal.toUpperCase();

        for (i = 0; i < hexadecimal.length(); i++) {
            ch = hexadecimal.charAt(i);

            if (Character.isDigit(ch) == false && ((int)ch >= 65 && (int)ch <= 70) == false) {
                binary = "Invalid Hexadecimal String";
                return binary;
            }else if ((int)ch >= 65 && (int)ch <= 70)
                returnedBinary = (int)ch - 55;
            else
                returnedBinary = Integer.parseInt(String.valueOf(ch));

            binary += decimalToBinary(returnedBinary);
        }

        return binary;
    }

    private static String decimalToBinary(int decimal) {
        String binaryString = "";

        while (decimal != 0) {
            binaryString = (decimal % 2) + binaryString;
            decimal /= 2;
        }
        while (binaryString.length() % 4 != 0) {
            binaryString = "0" + binaryString;
        }
        return binaryString;
    }
}
