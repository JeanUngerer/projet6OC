package com.buddyapp.paymybuddy.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MyTransaction {
    private double amount;
    private double fee;
    private String receiverUsername;
    private LocalDateTime date;
    private String description;
}
