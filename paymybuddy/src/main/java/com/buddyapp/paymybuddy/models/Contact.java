package com.buddyapp.paymybuddy.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    Long contactId;
    String firstName;
    String lastName;
    User user;
}
