package com.buddyapp.paymybuddy.DTOs;

import com.buddyapp.paymybuddy.models.User;
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
    User user;
    User friend;
}
