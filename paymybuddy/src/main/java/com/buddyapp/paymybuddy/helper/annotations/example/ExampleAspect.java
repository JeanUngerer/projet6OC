package com.buddyapp.paymybuddy.helper.annotations.example;

import com.buddyapp.paymybuddy.exception.ExceptionHandler;
import com.buddyapp.paymybuddy.mappers.TransactionMapper;
import com.buddyapp.paymybuddy.mappers.UserMapper;
import com.buddyapp.paymybuddy.models.Transaction;
import com.buddyapp.paymybuddy.models.User;
import com.buddyapp.paymybuddy.transaction.repository.TransactionRepository;
import com.buddyapp.paymybuddy.user.repository.UserRepository;
import com.buddyapp.paymybuddy.user.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
public class ExampleAspect {

    @Autowired
    private UserService userService;

    private TransactionMapper transactionMapper;

    private UserMapper userMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;
    private final PlatformTransactionManager transactionManager;

    public ExampleAspect(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Around("@annotation(TransactionFee)")
    public Object transactionFee(ProceedingJoinPoint joinPoint) throws Throwable {
        Double transactionFeeRate = 0.05;
        Optional<Transaction> otionnalTransactionToPlay = Arrays.stream(joinPoint.getArgs()).filter(obj -> obj instanceof Transaction).map(obj -> (Transaction) obj).findFirst();
        if (!otionnalTransactionToPlay.isPresent()){
            throw new ExceptionHandler("No transaction in function argument");
        }
        Transaction transactionToPlay = otionnalTransactionToPlay.get();

        Double amount = transactionToPlay.getAmount();

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        final Object[] proceed = new Object[1];

        transactionTemplate.execute(new TransactionCallback<Double>() {
            public Double doInTransaction(TransactionStatus transactionStatus) {
                try {
                    Double gasFee = amount * transactionFeeRate;
                    Double totalBurn = gasFee + amount;
                    User userInfo = userService.getUserById(transactionToPlay.getUser().getUserId());
                    if (userInfo.getBalance() < totalBurn){
                        throw new ExceptionHandler( "Not enough balance to pay transaction fees");
                    }
                    proceed[0] = joinPoint.proceed();
                    Transaction gasFeeTransaction = new Transaction(Long.valueOf(0), gasFee, "Transaction :: " + proceed[0].toString() + " :: gas Fee", new User(), userInfo);
                    transactionRepository.save(transactionMapper.modelToEntity(gasFeeTransaction));
                    userInfo = userService.getUserById(transactionToPlay.getUser().getUserId());
                    userInfo.setBalance(userInfo.getBalance()-gasFee);
                    userRepository.save(userMapper.modelToEntity(userInfo));
                    return totalBurn;
                } catch (Throwable t) {
                    transactionStatus.setRollbackOnly();
                    throw new ExceptionHandler( "Error during the gasFee balance attribution");
                }
            }
        });
        return proceed[0];
    }
}
