package com.buddyapp.paymybuddy.models;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    Long transactionId;
    Double amount;
    Double fee;
    String description;
    User trader;
    User user;
}
