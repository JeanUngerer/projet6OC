package com.buddyapp.paymybuddy.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    Long userId;
    String email;
    String password;
    String firstName;
    String lastName;
    String phoneNumber;
    Double balance;
    List<Contact> contacts;
    List<Transaction> transactions;
}
