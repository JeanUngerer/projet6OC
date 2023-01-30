package com.buddyapp.paymybuddy.transaction;

import com.buddyapp.paymybuddy.DTOs.TransactionToSendDTO;
import com.buddyapp.paymybuddy.DTOs.UserDTO;
import com.buddyapp.paymybuddy.constants.Provider;
import com.buddyapp.paymybuddy.entities.ContactEntity;
import com.buddyapp.paymybuddy.entities.TransactionEntity;
import com.buddyapp.paymybuddy.entities.UserEntity;
import com.buddyapp.paymybuddy.mappers.UserMapper;
import com.buddyapp.paymybuddy.models.Contact;
import com.buddyapp.paymybuddy.models.MyContact;
import com.buddyapp.paymybuddy.models.Transaction;
import com.buddyapp.paymybuddy.models.MyUser;
import com.buddyapp.paymybuddy.transaction.service.TransactionService;
import com.buddyapp.paymybuddy.user.repository.UserRepository;
import com.buddyapp.paymybuddy.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.buddyapp.paymybuddy.utils.ObjectAsJsonStrings.asJsonString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = true)
public class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserMapper userMapper;

    @Autowired
    TransactionService transactionService;


    @BeforeAll
    public static void prepareDB(){

    }

    @Test
    public void sendTransactionAPI() throws Exception
    {
        UserEntity sender = userRepository.save(new UserEntity(3l, Provider.LOCAL, "mail1@mail.com", "user1",passwordEncoder.encode("pass1"), "firsteName1",
                "lastName1", "0101010101", "ROLE_USER", 1000., new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));
        UserEntity trader = userRepository.save(new UserEntity(4l, Provider.LOCAL, "mail2@mail.com", "user2",passwordEncoder.encode("pass2"), "firsteName2",
                "lastName2", "0202020202", "ROLE_USER", 0., new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));

        String requestJson = "{ \"transactionId\":1, \"amount\":100.0, \"description\":\"testTransaction for 100\", " +
                "\"trader\":{\"userId\":3, \"email\":\"mail2@mail.com\", \"password\":\"$2a$10$7TWG.9Qo00cX6erxlMf1MOrPxCCc3o7jZpl4QpC9fBwyNG5..pNz6\", " +
                "\"firstName\":\"firstName2\", \"lastName\":\"lastName2\", \"phoneNumber\":\"0202020202\", \"balance\":0.0, \"contacts\":[], \"transactions\":[]}, " +
                "\"user\":{\"userId\":2, \"email\":\"mail1@mail.com\", \"password\":\"$2a$10$6iFqdLs8MtcNstc6BDOs9.rkbEyOwAoyn0jMJQ0KK0EdFIOqU9F0a\", " +
                "\"firstName\":\"firstName1\", \"lastName\":\"lastName1\", \"phoneNumber\":\"0101010101\", \"balance\":1000.0, \"contacts\":[], \"transactions\":[]}}";



        Transaction transaction = new Transaction(1L, 100.,0.,  "testTransaction for 100", userMapper.entityToModel(trader), userMapper.entityToModel(sender), LocalDateTime.now());
        System.out.println("TRANSACTION OBJECT : " + asJsonString(transaction));

        String token = obtainAccessToken(sender.getUserName(), "pass1");

        mockMvc.perform(put("/transaction/send")
                        .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(transaction)))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$.amount").value(100));
    }


    @Test
    public void sendMoneyAPI() throws Exception {
        UserEntity sender = userRepository.save(new UserEntity(3l, Provider.LOCAL, "mail1@mail.com", "user1",passwordEncoder.encode("pass1"), "firsteName1",
                "lastName1", "0101010101", "ROLE_USER", 1000., new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));
        UserEntity trader = userRepository.save(new UserEntity(4l, Provider.LOCAL, "mail2@mail.com", "user2",passwordEncoder.encode("pass2"), "firsteName2",
                "lastName2", "0202020202", "ROLE_USER", 0., new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));

        TransactionToSendDTO transaction = new TransactionToSendDTO(new MyContact("mail2@mail.com", "firsteName2",
                "lastName2","user2"), 100);

        String token = obtainAccessToken(sender.getUserName(), "pass1");

        mockMvc.perform(put("/transaction/sendmoney")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(transaction)))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$.message").value("Transaction sent to user : " + trader.getUserName()));

        assertEquals(895., userRepository.findByUserName("user1").get().getBalance());
        assertEquals(100., userRepository.findByUserName("user2").get().getBalance());
    }

    @Test
    public void myTransactionsAPI() throws Exception {
        UserEntity sender = userRepository.save(new UserEntity(3l, Provider.LOCAL, "mail1@mail.com", "user1",passwordEncoder.encode("pass1"), "firsteName1",
                "lastName1", "0101010101", "ROLE_USER", 1000., new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));
        UserEntity trader = userRepository.save(new UserEntity(4l, Provider.LOCAL, "mail2@mail.com", "user2",passwordEncoder.encode("pass2"), "firsteName2",
                "lastName2", "0202020202", "ROLE_USER", 0., new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));

        Transaction transaction = new Transaction(1L, 100.,0.,  "testTransaction for 100", userMapper.entityToModel(trader), userMapper.entityToModel(sender), LocalDateTime.now());
        Transaction transaction2 = new Transaction(2L, 50.,0.,  "testTransaction for 100", userMapper.entityToModel(sender), userMapper.entityToModel(trader), LocalDateTime.now());
        transactionService.sendTransaction(transaction);

        transactionService.sendTransaction(transaction2);

        String token = obtainAccessToken(sender.getUserName(), "pass1");

        mockMvc.perform(get("/transaction/mytransactions")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$.myTransactionList[0].receiverUsername").value("user2"))
                .andExpect(jsonPath("$.myTransactionList[1].receiverUsername").value("user1"))
                .andExpect(jsonPath("$.myTransactionList[0].amount").value(100.))
                .andExpect(jsonPath("$.myTransactionList[1].amount").value(50.))
        ;
    }

    public String obtainAccessToken(String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "userName");
        params.add("username", username);
        params.add("password", password);

        ResultActions result
                = mockMvc.perform(post("/token")
                        .params(params)
                        .with(httpBasic(username,password)))
                .andExpect(status().isOk());

        return result.andReturn().getResponse().getHeader("Token");
    }



}
