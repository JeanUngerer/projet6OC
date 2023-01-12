package com.buddyapp.paymybuddy.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegistrationDTO {
    private String username;
    private String mail;
    private String password;
    private String firstname;
    private String lastname;

}
