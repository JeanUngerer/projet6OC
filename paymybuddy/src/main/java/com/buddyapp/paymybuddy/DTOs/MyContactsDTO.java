package com.buddyapp.paymybuddy.DTOs;


import com.buddyapp.paymybuddy.models.MyContact;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyContactsDTO {
    private List<MyContact> myContacts;
}
