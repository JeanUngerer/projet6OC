package com.buddyapp.paymybuddy.models;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    Long contactId;
    MyUser myUser;
    MyUser friend;

}
