package com.buddyapp.paymybuddy.transaction;


import com.buddyapp.paymybuddy.DTOs.UserDTO;
import com.buddyapp.paymybuddy.models.Contact;
import com.buddyapp.paymybuddy.models.MyUser;
import com.buddyapp.paymybuddy.models.Transaction;
import com.buddyapp.paymybuddy.transaction.service.TransactionService;
import com.buddyapp.paymybuddy.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
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

        MyUser applicationReceiver = userService.createUser(new UserDTO( "Admin@mail.com", "useradmin", "useradmin","passAdmin", "AdminPayed",
                "AdminPayed", "0000000000", "ROLE_ADMIN", 0., new ArrayList<Contact>(), new ArrayList<Transaction>()));
        MyUser sender = userService.createUser(new UserDTO( "mail3@mail.com", "user3", "user3", "pass3", "firsteName3",
                "lastName3", "0303030303", "ROLE_USER", 1000., new ArrayList<Contact>(), new ArrayList<Transaction>()));
        MyUser trader = userService.createUser(new UserDTO( "mail4@mail.com", "user4", "user4", "pass4", "firsteName4",
                "lastName4", "0404040404", "ROLE_USER", 0., new ArrayList<Contact>(), new ArrayList<Transaction>()));



        Transaction transaction = new Transaction(1L, 100.,0., "testTransaction for 100", trader, sender, LocalDateTime.now());
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
