package com.buddyapp.paymybuddy.entities;

import com.buddyapp.paymybuddy.constants.Provider;
import com.buddyapp.paymybuddy.contact.repository.ContactRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", unique = true, nullable = false)
    Long userId;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(name = "email", unique = true, nullable = false, length = 50)
    String email;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    String userName;

    @Column(name = "displayes_login", nullable = false, length = 50)
    String login;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "firstname", unique = false, nullable = true, length = 50)
    String firstName;

    @Column(name = "lastname", unique = false, nullable = true, length = 50)
    String lastName;

    @Column(name = "phone_number", unique = false, nullable = true, length = 15)
    String phoneNumber;

    @Column(name = "role", nullable = false)
    String roles;

    @Column(name = "balance", nullable = false, length = 10)
    Double balance;

    @OneToMany(mappedBy = "contactId")
    List<ContactEntity> contacts;

    @OneToMany(mappedBy = "transactionId")
    List<TransactionEntity> transactions;
}
