package com.buddyapp.paymybuddy.DTOs;

import com.buddyapp.paymybuddy.models.User;
import lombok.*;

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
    User trader;
    User user;
}
