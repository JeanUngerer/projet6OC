package com.buddyapp.paymybuddy.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyContact {
    private String mail;
    private String firstname;
    private String lastname;
    private String username;
}
