package com.buddyapp.paymybuddy.transaction;

import com.buddyapp.paymybuddy.DTOs.UserDTO;
import com.buddyapp.paymybuddy.models.Contact;
import com.buddyapp.paymybuddy.models.Transaction;
import com.buddyapp.paymybuddy.models.MyUser;
import com.buddyapp.paymybuddy.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static com.buddyapp.paymybuddy.utils.ObjectAsJsonStrings.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserService userService;


    @BeforeAll
    public static void prepareDB(){

    }

    @Test
    public void sendTransactionAPI() throws Exception
    {
        MyUser sender = userService.createUser(new UserDTO( "mail1@mail.com", "user1","pass1", "firsteName1",
                "lastName1", "0101010101", "ROLE_USER", 1000., new ArrayList<Contact>(), new ArrayList<Transaction>()));
        MyUser trader = userService.createUser(new UserDTO( "mail2@mail.com", "user2","pass2", "firsteName2",
                "lastName2", "0202020202", "ROLE_USER", 0., new ArrayList<Contact>(), new ArrayList<Transaction>()));

        String requestJson = "{ \"transactionId\":1, \"amount\":100.0, \"description\":\"testTransaction for 100\", " +
                "\"trader\":{\"userId\":3, \"email\":\"mail2@mail.com\", \"password\":\"$2a$10$7TWG.9Qo00cX6erxlMf1MOrPxCCc3o7jZpl4QpC9fBwyNG5..pNz6\", " +
                "\"firstName\":\"firstName2\", \"lastName\":\"lastName2\", \"phoneNumber\":\"0202020202\", \"balance\":0.0, \"contacts\":[], \"transactions\":[]}, " +
                "\"user\":{\"userId\":2, \"email\":\"mail1@mail.com\", \"password\":\"$2a$10$6iFqdLs8MtcNstc6BDOs9.rkbEyOwAoyn0jMJQ0KK0EdFIOqU9F0a\", " +
                "\"firstName\":\"firstName1\", \"lastName\":\"lastName1\", \"phoneNumber\":\"0101010101\", \"balance\":1000.0, \"contacts\":[], \"transactions\":[]}}";



        Transaction transaction = new Transaction(1L, 100.,0.,  "testTransaction for 100", trader, sender);
        System.out.println("TRANSACTION OBJECT : " + asJsonString(transaction));

        mockMvc.perform(put("/transaction/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(transaction)))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$.amount").value(100));


    }

    @Test
    public void dummyTestAPI() throws Exception{

        MyUser sender = userService.createUser(new UserDTO( "mail3@mail.com", "user3","pass3", "firsteName3",
                "lastName3", "0303030303", "ROLE_USER", 1000., new ArrayList<Contact>(), new ArrayList<Transaction>()));
        MyUser trader = userService.createUser(new UserDTO( "mail4@mail.com", "user4","pass4", "firsteName4",
                "lastName4", "0404040404", "ROLE_USER", 0., new ArrayList<Contact>(), new ArrayList<Transaction>()));

        String requestJson = "{ \"transactionId\":1, \"amount\":100.0, \"description\":\"testTransaction for 100\", " +
                "\"trader\":{\"userId\":3, \"email\":\"mail2@mail.com\", \"password\":\"$2a$10$7TWG.9Qo00cX6erxlMf1MOrPxCCc3o7jZpl4QpC9fBwyNG5..pNz6\", " +
                "\"firstName\":\"firstName2\", \"lastName\":\"lastName2\", \"phoneNumber\":\"0202020202\", \"balance\":0.0, \"contacts\":{}, \"transactions\":{}}, " +
                "\"user\":{\"userId\":2, \"email\":\"mail1@mail.com\", \"password\":\"$2a$10$6iFqdLs8MtcNstc6BDOs9.rkbEyOwAoyn0jMJQ0KK0EdFIOqU9F0a\", " +
                "\"firstName\":\"firstName1\", \"lastName\":\"lastName1\", \"phoneNumber\":\"0101010101\", \"balance\":1000.0, \"contacts\":{}, \"transactions\":{}}}";


        Transaction transaction = new Transaction(1L, 100.,0., "testTransaction for 100", trader, sender);
        System.out.println("TRANSACTION OBJECT : " + asJsonString(transaction));

        mockMvc.perform(get("/transaction/dummy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(transaction)))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$.amount").value(100));

    }


}
