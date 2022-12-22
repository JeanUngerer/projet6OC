package com.buddyapp.paymybuddy.models;


import lombok.*;


import java.util.List;


@Getter
@Setter
@ToString
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
    //List<User> contactsOf;
    List<Transaction> transactions;
}
