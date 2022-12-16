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
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
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

import static com.buddyapp.paymybuddy.constants.FeesRate.FeeRate;

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
    public Object transactionFee(ProceedingJoinPoint joinPoint) throws Throwable {
        Double transactionFeeRate = FeeRate;
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

                    userInfo.setBalance(userInfo.getBalance()-totalBurn);
                    userRepository.save(userMapper.modelToEntity(userInfo));

                    proceed[0] = joinPoint.proceed();


                    Transaction gasFeeTransaction = new Transaction(Long.valueOf(0), gasFee, "Transaction :: " + proceed[0].toString() + " :: gas Fee",
                            userService.getUserById(1L), userInfo);

                    transactionRepository.save(transactionMapper.modelToEntity(gasFeeTransaction));

                    return totalBurn;
                } catch (Throwable t) {
                    transactionStatus.setRollbackOnly();
                    throw new ExceptionHandler( "Error during the gasFee balance attribution, " + t.getMessage());
                }
            }
        });
        return proceed[0];
    }
}
