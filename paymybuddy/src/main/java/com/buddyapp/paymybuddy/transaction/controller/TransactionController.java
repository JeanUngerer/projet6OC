package com.buddyapp.paymybuddy.transaction.controller;

import com.buddyapp.paymybuddy.DTOs.TransactionDTO;
import com.buddyapp.paymybuddy.mappers.TransactionMapper;
import com.buddyapp.paymybuddy.transaction.service.TransactionService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequestMapping("transaction")
public class TransactionController {

    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/allfrom/{id}")
    public ResponseEntity<List<TransactionDTO>> findAllUserTransactions(@PathVariable("id") Long id) {
        return ResponseEntity.ok(transactionMapper.modelsToDtos(transactionService.findAllUserTransactions(id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> findTransactionById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(transactionMapper.modelToDto(transactionService.findTransactionById(id)));
    }

    @PutMapping("/send")
    @RolesAllowed("ADMIN")
    public ResponseEntity<TransactionDTO> findTransactionById(@RequestBody TransactionDTO dto) {
        return ResponseEntity.ok(transactionMapper.modelToDto(transactionService.sendTransaction(transactionMapper.dtoToModel(dto))));
    }

    @GetMapping("/dummy")
    public ResponseEntity<TransactionDTO> dummyController(@RequestBody TransactionDTO dto){
        return ResponseEntity.ok(dto);
    }
}
