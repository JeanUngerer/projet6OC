package com.buddyapp.paymybuddy.helper.annotations.example;

import com.buddyapp.paymybuddy.exception.ExceptionHandler;
import com.buddyapp.paymybuddy.mappers.UserMapper;
import com.buddyapp.paymybuddy.models.MyUser;
import com.buddyapp.paymybuddy.models.Transaction;
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
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.Optional;

import static com.buddyapp.paymybuddy.constants.FeesRate.FEE_RATE;

@Aspect
@Component
public class ExampleAspect {

    @Autowired
    private UserService userService;

    UserMapper userMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;
    private final PlatformTransactionManager transactionManager;

    public ExampleAspect(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Around("@annotation(TransactionFee)")
    public Object transactionFeeAround(ProceedingJoinPoint proceedingJoinPoint){
        Object[] arguments = proceedingJoinPoint.getArgs();
        final Object[] proceed = new Object[1];
        Optional<Transaction> otionnalTransactionToPlay = Arrays.stream(arguments).filter(obj -> obj instanceof Transaction).map(obj -> (Transaction) obj).findFirst();
        if (!otionnalTransactionToPlay.isPresent()){
            throw new ExceptionHandler("No transaction in function argument");
        }

        Transaction transactionToPlay = otionnalTransactionToPlay.get();

        Double amount = transactionToPlay.getAmount();

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        Transaction transactionPlayed = transactionTemplate.execute((transactionStatus) -> {
            return doInTransaction(transactionStatus, amount, transactionToPlay, proceedingJoinPoint);
        });

        return transactionPlayed;
    }

    private Transaction doInTransaction(TransactionStatus transactionStatus, Double amount, Transaction transactionToPlay, ProceedingJoinPoint proceedingJoinPoint) {
        try {
            final Object[] proceed = new Object[1];
            Object[] arguments = proceedingJoinPoint.getArgs();
            Double gasFee = amount * FEE_RATE;
            Double totalBurn = gasFee + amount;
            MyUser myUserInfo = userService.getUserById(transactionToPlay.getMyUser().getUserId());
            transactionToPlay.setFee(gasFee);
            int arglength = arguments.length;
            for (int i = 0; i< arglength; i++){
                if (arguments[i] instanceof Transaction){
                    arguments[i] = transactionToPlay;
                    break;
                }
            }

            proceed[0] = proceedingJoinPoint.proceed();
            myUserInfo = userService.getUserById(transactionToPlay.getMyUser().getUserId());
            myUserInfo.setBalance(myUserInfo.getBalance()-gasFee);
            userRepository.save(userMapper.modelToEntity(myUserInfo));

            return transactionToPlay;

        } catch (Throwable t) {
            transactionStatus.setRollbackOnly();
            throw new ExceptionHandler( "Error during the gasFee balance attribution, " + t.getMessage());
        }
    }
}
