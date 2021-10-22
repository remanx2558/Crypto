package com.paytm.pgplus.crypto.wallet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SenderWallet {
    private int balance;
    private UUID address = UUID.randomUUID();
    private String publicKey;
    private String signature;
}
