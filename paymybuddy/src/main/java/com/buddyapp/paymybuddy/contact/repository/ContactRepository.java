package com.buddyapp.paymybuddy.contact.repository;

import com.buddyapp.paymybuddy.entities.ContactEntity;
import com.buddyapp.paymybuddy.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
    Optional<ContactEntity> findByUserAndFriend(UserEntity user, UserEntity friend);

    Optional<List<ContactEntity>> findAllByUser_UserId(Long userId);
}