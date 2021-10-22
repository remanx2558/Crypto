package com.paytm.pgplus.crypto.wallet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SenderWallet {
    private int balance;
    private Address address;
    private String publicKey;
    private String signature;
}
