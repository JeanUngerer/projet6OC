package com.buddyapp.paymybuddy.transaction.repository;

import com.buddyapp.paymybuddy.entities.TransactionEntity;
import com.buddyapp.paymybuddy.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    Optional<List<TransactionEntity>> findAllByUser(UserEntity userEntity);

    Optional<List<TransactionEntity>> findAllByUser_UserId(Long userId);

    Optional<List<TransactionEntity>> findAllByTrader_UserId(Long userId);
}
