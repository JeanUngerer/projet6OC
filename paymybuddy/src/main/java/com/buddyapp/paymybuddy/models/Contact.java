package com.buddyapp.paymybuddy.models;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    Long contactId;
    String firstName;
    String lastName;
    User user;
}
