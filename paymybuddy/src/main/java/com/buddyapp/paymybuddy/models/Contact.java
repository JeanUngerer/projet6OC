package com.buddyapp.paymybuddy.models;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    Long contactId;
    User user;
    User friend;

}
