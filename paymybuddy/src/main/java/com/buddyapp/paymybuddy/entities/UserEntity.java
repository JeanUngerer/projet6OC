package com.buddyapp.paymybuddy.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "email", unique = true, nullable = false, length = 50)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "firstname", unique = false, nullable = false, length = 50)
    String firstName;

    @Column(name = "lastname", unique = false, nullable = false, length = 50)
    String lastName;

    @Column(name = "phone_number", unique = false, nullable = false, length = 15)
    String phoneNumber;

    @Column(name = "balance", nullable = false, length = 10)
    Double balance;

    @OneToMany(mappedBy = "contactId")
    List<ContactEntity> contacts;

    @OneToMany(mappedBy = "transactionId")
    List<TransactionEntity> transactions;

    /*
    @ManyToMany
    @JoinTable(name="tbl_contacts",
            joinColumns=@JoinColumn(name="friendId"),
            inverseJoinColumns=@JoinColumn(name="personId")
    )
    List<UserEntity> contacts;

    @ManyToMany
    @JoinTable(name="tbl_contacts",
            joinColumns=@JoinColumn(name="personId"),
            inverseJoinColumns=@JoinColumn(name="friendId")
    )
    List<UserEntity> contactsOf;

     */
}
