package com.buddyapp.paymybuddy.transaction.service;


import com.buddyapp.paymybuddy.DTOs.MyTransactionsDTO;
import com.buddyapp.paymybuddy.entities.TransactionEntity;
import com.buddyapp.paymybuddy.entities.UserEntity;
import com.buddyapp.paymybuddy.exception.ExceptionHandler;
import com.buddyapp.paymybuddy.helper.annotations.feeTax.TransactionFee;
import com.buddyapp.paymybuddy.mappers.CustomMappers;
import com.buddyapp.paymybuddy.mappers.TransactionMapper;
import com.buddyapp.paymybuddy.mappers.UserMapper;
import com.buddyapp.paymybuddy.models.MyUser;
import com.buddyapp.paymybuddy.models.Transaction;
import com.buddyapp.paymybuddy.transaction.repository.TransactionRepository;
import com.buddyapp.paymybuddy.user.repository.UserRepository;
import com.buddyapp.paymybuddy.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
public class TransactionService {

    private final PlatformTransactionManager transactionManager;

    @Autowired
    private UserService userService;

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionMapper transactionMapper;

    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    CustomMappers customMapper;

    public List<Transaction> findAllUserTransactions(Long userId) {
        try {
            log.info("findAllUserTransactions");
            UserEntity userEntity = userRepository.findById(userId).orElseThrow(()
                    -> new ExceptionHandler("We could not find your account"));
            return transactionMapper.entitiesToModel(transactionRepository.findAllByUser(userEntity).orElseThrow());
        } catch (Exception e) {
            log.error("Couldn't find all transaction: " + e.getMessage());
            throw new ExceptionHandler("We could not find your transactions");
        }
    }

    public Transaction findTransactionById(Long id) {
        try {
            log.info("findTransactionById - id: " + id.toString());
            TransactionEntity transactionEntity = transactionRepository.findById(id).orElseThrow(()
                    -> new ExceptionHandler("We could not find your transaction"));
            return transactionMapper.entityToModel(transactionEntity);
        } catch (Exception e) {
            log.error("Couldn't find transaction: " + e.getMessage());
            throw new ExceptionHandler("We could not find your transaction");
        }
    }

    @TransactionFee
    public Transaction sendTransaction(Transaction transaction) {
        try {
            log.info("sendTransaction - transaction: " + transaction.toString());
            MyUser trader = userService.getUserById(transaction.getTrader().getUserId());
            MyUser myUser = userService.getUserById(transaction.getMyUser().getUserId());

            if (myUser.getBalance() < (transaction.getAmount())) {
                throw new ExceptionHandler("Not enough money");
            }

            trader.setBalance(trader.getBalance() + transaction.getAmount());

            myUser.setBalance(myUser.getBalance() - transaction.getAmount());

            transaction.setMyUser(myUser);

            transaction.setTrader(trader);

            transaction.setDate(LocalDateTime.now());

            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);


            transactionTemplate.execute(new TransactionCallback<Void>() {
                public Void doInTransaction(TransactionStatus transactionStatus) {
                    try {
                        transactionRepository.save(transactionMapper.modelToEntity(transaction));

                        userRepository.save(userMapper.modelToEntity(myUser));
                        userRepository.save(userMapper.modelToEntity(trader));

                    } catch (Throwable t) {
                        transactionStatus.setRollbackOnly();
                        log.error("Error during the new balances attribution : " + t.getMessage());
                        throw new ExceptionHandler( "Error during the new balances attribution : " + t.getMessage());
                    }
                    return null;
                }
            });

        } catch (Exception e) {
            log.error("Couldn't receive transaction: " + e.getMessage());
            throw new ExceptionHandler("We could not complete your transaction");
        }
        return  transaction;
    }

    public MyTransactionsDTO myTransactions(MyUser me) {
        try {
            log.info("myTransactions - " + me.getUserName());
            MyTransactionsDTO myTransactionsDTO = new MyTransactionsDTO();
            myTransactionsDTO.setMyTransactionList(
                    customMapper.transactionsToMyTransactions(
                            transactionMapper.entitiesToModel(
                                    transactionRepository.findAllByUser_UserId(me.getUserId()).get())

                    ));

            myTransactionsDTO.getMyTransactionList().addAll(customMapper.transactionsToMyTransactions(
                    transactionMapper.entitiesToModel(
                            transactionRepository.findAllByTrader_UserId(me.getUserId()).get()
                    )));

            return myTransactionsDTO;
        } catch (Exception e) {
            log.error("Couldn't compute your transactions list: " + e.getMessage());
            throw new ExceptionHandler("We could not compute your transactions list");
        }
    }
}
