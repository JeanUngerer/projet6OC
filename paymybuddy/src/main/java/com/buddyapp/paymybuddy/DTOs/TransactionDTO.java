package com.buddyapp.paymybuddy.DTOs;

import com.buddyapp.paymybuddy.models.MyUser;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    Long transactionId;
    Double amount;
    Double fee;
    String description;
    MyUser trader;
    MyUser myUser;
    LocalDateTime date;
}
