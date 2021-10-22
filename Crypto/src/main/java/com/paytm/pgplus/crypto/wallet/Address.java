package com.paytm.pgplus.crypto.wallet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String state;
    private String city;
    private String pinCode;
    private String street;
    private int Hno;
}
