package com.buddyapp.paymybuddy.contacts;


import com.buddyapp.paymybuddy.DTOs.UserDTO;
import com.buddyapp.paymybuddy.contact.service.ContactService;
import com.buddyapp.paymybuddy.models.Contact;
import com.buddyapp.paymybuddy.models.MyUser;
import com.buddyapp.paymybuddy.models.Transaction;
import com.buddyapp.paymybuddy.user.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class ContactControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ContactService contactService;
    @Autowired
    private UserService userService;


    @BeforeAll
    public static void prepareDB(){
    }

    //@Test
    public void addContactByUsernameAPI() throws Exception {
        MyUser sender = userService.createUser(new UserDTO( "mail1@mail.com", "user1","pass1", "firsteName1",
                "lastName1", "0101010101", "ROLE_USER", 1000., new ArrayList<Contact>(), new ArrayList<Transaction>()));
        MyUser trader = userService.createUser(new UserDTO( "mail2@mail.com", "user2","pass2", "firsteName2",
                "lastName2", "0202020202", "ROLE_USER", 0., new ArrayList<Contact>(), new ArrayList<Transaction>()));




    }
}
