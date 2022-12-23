package com.buddyapp.paymybuddy.helper.annotations.example;

import com.buddyapp.paymybuddy.exception.ExceptionHandler;
import com.buddyapp.paymybuddy.mappers.TransactionMapper;
import com.buddyapp.paymybuddy.mappers.TransactionMapperImpl;
import com.buddyapp.paymybuddy.mappers.UserMapper;
import com.buddyapp.paymybuddy.mappers.UserMapperImpl;
import com.buddyapp.paymybuddy.models.Transaction;
import com.buddyapp.paymybuddy.models.User;
import com.buddyapp.paymybuddy.transaction.repository.TransactionRepository;
import com.buddyapp.paymybuddy.user.repository.UserRepository;
import com.buddyapp.paymybuddy.user.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.Optional;

import static com.buddyapp.paymybuddy.constants.FeesRate.FEE_RATE;

@Aspect
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class ExampleAspect {

    @Autowired
    private UserService userService;

    private TransactionMapper transactionMapper = new TransactionMapperImpl();

    private UserMapper userMapper = new UserMapperImpl();

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

    /*
    @Before("@annotation(TransactionFee)")
    public Object transactionFeeBefore(JoinPoint joinPoint) throws Throwable {
        Optional<Transaction> otionnalTransactionToPlay = Arrays.stream(joinPoint.getArgs()).filter(obj -> obj instanceof Transaction).map(obj -> (Transaction) obj).findFirst();
        if (!otionnalTransactionToPlay.isPresent()){
            throw new ExceptionHandler("No transaction in function argument");
        }

        final Object[] proceed = new Object[1];

        return proceed[0];
    }

    @AfterReturning(value = "@annotation(TransactionFee)", returning = "returnObject")
    public void transactionFeeAfter(JoinPoint joinPoint, Object returnObject){

        Optional<Transaction> otionnalTransactionToPlay = Arrays.stream(joinPoint.getArgs()).filter(obj -> obj instanceof Transaction).map(obj -> (Transaction) obj).findFirst();
        if (!otionnalTransactionToPlay.isPresent()){
            throw new ExceptionHandler("No transaction in function argument");
        }
        Transaction transactionToPlay = otionnalTransactionToPlay.get();

        Double amount = transactionToPlay.getAmount();

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        transactionTemplate.execute((transactionStatus) -> {
            doInTransaction(transactionStatus, amount, transactionToPlay);

            return null;
        });
    }*/

    public Transaction doInTransaction(TransactionStatus transactionStatus, Double amount, Transaction transactionToPlay, ProceedingJoinPoint proceedingJoinPoint) {
        try {
            final Object[] proceed = new Object[1];
            Object[] arguments = proceedingJoinPoint.getArgs();
            Double gasFee = amount * FEE_RATE;
            Double totalBurn = gasFee + amount;
            User userInfo = userService.getUserById(transactionToPlay.getUser().getUserId());
            transactionToPlay.setFee(gasFee);
            int arglength = arguments.length;
            for (int i = 0; i< arglength; i++){
                if (arguments[i] instanceof Transaction){
                    arguments[i] = transactionToPlay;
                    break;
                }
            }

            proceed[0] = proceedingJoinPoint.proceed();
            userInfo = userService.getUserById(transactionToPlay.getUser().getUserId());
            userInfo.setBalance(userInfo.getBalance()-gasFee);
            userRepository.save(userMapper.modelToEntity(userInfo));

            return transactionToPlay;

        } catch (Throwable t) {
            transactionStatus.setRollbackOnly();
            throw new ExceptionHandler( "Error during the gasFee balance attribution, " + t.getMessage());
        }
    }
}
