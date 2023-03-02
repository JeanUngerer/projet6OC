package com.buddyapp.paymybuddy.user;

import com.buddyapp.paymybuddy.DTOs.AddFundsDTO;
import com.buddyapp.paymybuddy.constants.Provider;
import com.buddyapp.paymybuddy.entities.ContactEntity;
import com.buddyapp.paymybuddy.entities.TransactionEntity;
import com.buddyapp.paymybuddy.entities.UserEntity;
import com.buddyapp.paymybuddy.mappers.UserMapper;
import com.buddyapp.paymybuddy.transaction.service.TransactionService;
import com.buddyapp.paymybuddy.user.repository.UserRepository;
import com.buddyapp.paymybuddy.user.service.UserService;
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

import java.util.ArrayList;

import static com.buddyapp.paymybuddy.utils.ObjectAsJsonStrings.asJsonString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = true)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @Test
    public void myBalanceAPI() throws Exception {
        UserEntity sender = userRepository.save(new UserEntity(3l, Provider.LOCAL, "mail1@mail.com", "user1", "user1",passwordEncoder.encode("pass1"), "firsteName1",
                "lastName1", "0101010101", "ROLE_USER", 1000., new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));

        String token = obtainAccessToken(sender.getUserName(), "pass1");

        mockMvc.perform(get("/user/mybalance")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$.balance").value(1000.));
    }

    @Test
    public void myAddFundsAPI() throws Exception {
        UserEntity sender = userRepository.save(new UserEntity(3l, Provider.LOCAL, "mail1@mail.com", "user1", "user1",passwordEncoder.encode("pass1"), "firsteName1",
                "lastName1", "0101010101", "ROLE_USER", 1000., new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));

        String token = obtainAccessToken(sender.getUserName(), "pass1");

        AddFundsDTO addFundsDTO = new AddFundsDTO(100.);

        mockMvc.perform(put("/user/addfunds")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addFundsDTO)))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$.balance").value(1100.));
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
