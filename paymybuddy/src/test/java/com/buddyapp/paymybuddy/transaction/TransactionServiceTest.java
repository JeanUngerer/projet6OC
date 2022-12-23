package com.buddyapp.paymybuddy.transaction;


import com.buddyapp.paymybuddy.DTOs.UserDTO;
import com.buddyapp.paymybuddy.models.Contact;
import com.buddyapp.paymybuddy.models.Transaction;
import com.buddyapp.paymybuddy.models.User;
import com.buddyapp.paymybuddy.transaction.service.TransactionService;
import com.buddyapp.paymybuddy.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class TransactionServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Test
    public void sendTransactionServiceTest(){
        Double FeesRate = 0.05;

        User applicationReceiver = userService.createUser(new UserDTO(1L, "Admin@mail.com","passAdmin", "AdminPayed",
                "AdminPayed", "0000000000", 0., new ArrayList<Contact>(), new ArrayList<Transaction>()));
        User sender = userService.createUser(new UserDTO(4L, "mail3@mail.com","pass3", "firsteName3",
                "lastName3", "0303030303", 1000., new ArrayList<Contact>(), new ArrayList<Transaction>()));
        User trader = userService.createUser(new UserDTO(5L, "mail4@mail.com","pass4", "firsteName4",
                "lastName4", "0404040404", 0., new ArrayList<Contact>(), new ArrayList<Transaction>()));



        Transaction transaction = new Transaction(1L, 100.,0., "testTransaction for 100", trader, sender);
        Double expectedNewBalance = sender.getBalance()-transaction.getAmount()-transaction.getAmount()* FeesRate;

        System.out.println("DESCRIPTION LENGTH : " + transaction.getDescription().length());

        transactionService.sendTransaction(transaction);

        sender = userService.getUserByEmail("mail3@mail.com");
        trader = userService.getUserByEmail("mail4@mail.com");


        List<Transaction> transactionsDone =  transactionService.findAllUserTransactions(sender.getUserId());

        assertEquals( expectedNewBalance, sender.getBalance());
        assertEquals(100., trader.getBalance());
    }

}
