package com.buddyapp.paymybuddy.auth;

import com.buddyapp.paymybuddy.DTOs.UserDTO;
import com.buddyapp.paymybuddy.models.Contact;
import com.buddyapp.paymybuddy.models.MyUser;
import com.buddyapp.paymybuddy.models.Transaction;
import com.buddyapp.paymybuddy.user.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static com.buddyapp.paymybuddy.utils.ObjectAsJsonStrings.asJsonString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = true)
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserService userService;


    @BeforeAll
    public static void prepareDB(){

    }

    @Test
    void rootWhenUnauthenticatedThen401() throws Exception {
        this.mockMvc.perform(get("/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rootWhenAuthenticatedThenSaysHelloUser() throws Exception {

        MyUser sender = userService.createUser(new UserDTO( "mailHello@mail.com", "hello1", "pass1", "firsteName1",
                "lastName1", "0101010101", "ROLE_ADMIN", 1000., new ArrayList<Contact>(), new ArrayList<Transaction>()));

        MvcResult result = this.mockMvc.perform(post("/token")
                        .with(httpBasic(sender.getUserName(), "pass1")))
                .andExpect(status().isOk())
                .andReturn();

        String token = result.getResponse().getHeader("Authentication");

        this.mockMvc.perform(get("/user")
                        .header("Authorization", "Bearer " + token))
                .andExpect(content().string("Welcome hello1"));
    }

    @Test
    @WithMockUser
    public void rootWithMockUserStatusIsOK() throws Exception {
        this.mockMvc.perform(get("/home")).andExpect(status().isOk());
    }

}
