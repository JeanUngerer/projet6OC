package com.buddyapp.paymybuddy.models;

import lombok.*;

import java.time.LocalDateTime;

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
    MyUser trader;
    MyUser myUser;
    LocalDateTime date;
}
