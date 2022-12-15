package com.buddyapp.paymybuddy.contact.repository;

import com.buddyapp.paymybuddy.entities.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
}