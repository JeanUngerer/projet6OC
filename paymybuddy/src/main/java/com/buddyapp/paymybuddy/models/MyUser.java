package com.buddyapp.paymybuddy.models;


import lombok.*;
import org.springframework.security.oauth2.core.user.OAuth2User;


import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MyUser {
    Long userId;
    String provider;
    String email;
    String userName;
    String password;
    String firstName;
    String lastName;
    String phoneNumber;
    String roles;
    Double balance;
    List<Contact> contacts;
    //List<User> contactsOf;
    List<Transaction> transactions;
}
