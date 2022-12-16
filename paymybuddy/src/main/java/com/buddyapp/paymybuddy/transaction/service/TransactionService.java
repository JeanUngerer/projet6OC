package com.buddyapp.paymybuddy.transaction.service;


import com.buddyapp.paymybuddy.DTOs.TransactionDTO;
import com.buddyapp.paymybuddy.entities.TransactionEntity;
import com.buddyapp.paymybuddy.entities.UserEntity;
import com.buddyapp.paymybuddy.exception.ExceptionHandler;
import com.buddyapp.paymybuddy.helper.annotations.example.TransactionFee;
import com.buddyapp.paymybuddy.mappers.TransactionMapper;
import com.buddyapp.paymybuddy.models.Transaction;
import com.buddyapp.paymybuddy.models.User;
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

    TransactionRepository transactionRepository;
    TransactionMapper transactionMapper;
    UserRepository userRepository;

    public List<Transaction> findAllUserTransactions(Long userId) {
        try {
            UserEntity userEntity = userRepository.findById(userId).orElseThrow(()
                    -> new ExceptionHandler("We could not find your account"));
            return transactionMapper.entitiesToModel(transactionRepository.findAllByUser(userEntity));
        } catch (Exception e) {
            log.error("Couldn't find all transaction: " + e.getMessage());
            throw new ExceptionHandler("We could not find your transactions");
        }
    }

    public Transaction findTransactionById(Long id) {
        try {
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

            User trader = userService.getUserById(transaction.getTrader().getUserId());
            User user = userService.getUserById(transaction.getUser().getUserId());

            if (user.getBalance() < transaction.getAmount()) {
                throw new ExceptionHandler("Not enough money");
            }

            trader.setBalance(trader.getBalance() + transaction.getAmount());

            user.setBalance(user.getBalance() - transaction.getAmount());

            transaction.setUser(user);


            transaction.setTrader(trader);

            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);


          /*  transactionTemplate.execute(new TransactionCallback<Void>() {
                public Void doInTransaction(TransactionStatus transactionStatus) {*/
                    try {
                        transactionRepository.save(transactionMapper.modelToEntity(transaction));
                        return transaction;
                    } catch (Throwable t) {
                        //transactionStatus.setRollbackOnly();
                        throw new ExceptionHandler( "Error during the new balances attribution : " + t.getMessage());
                    }
                    //return null;
               // }
          //  });

        } catch (Exception e) {
            log.error("Couldn't receive transaction: " + e.getMessage());
            throw new ExceptionHandler("We could not complete your transaction");
        }
    }

}
