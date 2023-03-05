package com.buddyapp.paymybuddy.transaction.controller;

import com.buddyapp.paymybuddy.DTOs.MessageDTO;
import com.buddyapp.paymybuddy.DTOs.MyTransactionsDTO;
import com.buddyapp.paymybuddy.DTOs.TransactionDTO;
import com.buddyapp.paymybuddy.DTOs.TransactionToSendDTO;
import com.buddyapp.paymybuddy.auth.service.TokenService;
import com.buddyapp.paymybuddy.mappers.CustomMappers;
import com.buddyapp.paymybuddy.mappers.TransactionMapper;
import com.buddyapp.paymybuddy.models.MyUser;
import com.buddyapp.paymybuddy.models.Transaction;
import com.buddyapp.paymybuddy.transaction.service.TransactionService;
import com.buddyapp.paymybuddy.user.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequestMapping("transaction")
public class TransactionController {

    private TransactionMapper transactionMapper;

    private CustomMappers customMapper;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Full CRUD for admin
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/allfrom/{id}")
    public ResponseEntity<List<TransactionDTO>> findAllUserTransactions(@PathVariable("id") Long id) {
        return ResponseEntity.ok(transactionMapper.modelsToDtos(transactionService.findAllUserTransactions(id)));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> findTransactionById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(transactionMapper.modelToDto(transactionService.findTransactionById(id)));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PutMapping("/send")
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO dto) {
        Transaction response = transactionService.sendTransaction(transactionMapper.dtoToModel(dto));
        return ResponseEntity.ok(transactionMapper.modelToDto(response));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // User accessible
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN', 'SCOPE_OAUTH2_USER')")
    @PutMapping("/sendmoney")
    public ResponseEntity<MessageDTO> sendMoney(@RequestHeader("Authorization") String requestTokenHeader, @RequestBody TransactionToSendDTO transactionDTO){
        MyUser me = userService.getUserByUserName(tokenService.decodeTokenUsername(requestTokenHeader));
        MyUser target = userService.getUserByUserName(transactionDTO.getSendTo().getIdentifier());
        Transaction transaction = new Transaction(null, transactionDTO.getAmount(), 0., transactionDTO.getDescription(), target, me, LocalDateTime.now());
        transactionService.sendTransaction(transaction);
        return ResponseEntity.ok(new MessageDTO("Transaction sent to user : " + target.getUserName()));
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN', 'SCOPE_OAUTH2_USER')")
    @GetMapping("/mytransactions")
    public ResponseEntity<MyTransactionsDTO> getMyTransactions(@RequestHeader("Authorization") String requestTokenHeader){
        MyUser me = userService.getUserByUserName(tokenService.decodeTokenUsername(requestTokenHeader));
        return ResponseEntity.ok( transactionService.myTransactions(me) );
    }
}
