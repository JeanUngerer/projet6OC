package com.buddyapp.paymybuddy.DTOs;

import com.buddyapp.paymybuddy.models.Contact;
import com.buddyapp.paymybuddy.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    String email;
    String userName;
    String login;
    String password;
    String firstName;
    String lastName;
    String phoneNumber;
    String roles;
    Double balance;
    List<Contact> contacts;
    List<Transaction> transactions;
}
