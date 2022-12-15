package com.buddyapp.paymybuddy.transaction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;


    //@Test
    public void createPersonAPI() throws Exception
    {
        String requestJson = "{ \"firstName\":\"mee\", \"lastName\":\"tooo\", \"address\":\"1509 Culver St\", \"city\":\"Culver\"," +
                " \"zip\":\"97451\", \"phone\":\"841-874-6512\", \"email\":\"mtooo@email.com\" }";

        mockMvc.perform( MockMvcRequestBuilders
                        .post("/api/transaction/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("24")));


    }
}
