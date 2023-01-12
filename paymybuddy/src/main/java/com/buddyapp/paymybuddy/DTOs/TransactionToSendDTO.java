package com.buddyapp.paymybuddy.DTOs;

import com.buddyapp.paymybuddy.models.MyContact;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionToSendDTO {
    private MyContact sendTo;
    private double amount;
}
