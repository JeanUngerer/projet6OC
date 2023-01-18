package com.buddyapp.paymybuddy.DTOs;

import com.buddyapp.paymybuddy.models.MyUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {
    Long contactId;
    MyUser user;
    MyUser friend;
}
