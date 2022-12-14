package com.buddyapp.paymybuddy.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    Long transactionId;
    Double amount;
    String description;
    User trader;
    User user;
}
