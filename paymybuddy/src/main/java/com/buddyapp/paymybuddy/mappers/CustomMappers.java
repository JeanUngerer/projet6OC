package com.buddyapp.paymybuddy.mappers;


import com.buddyapp.paymybuddy.contact.repository.ContactRepository;
import com.buddyapp.paymybuddy.entities.ContactEntity;
import com.buddyapp.paymybuddy.entities.TransactionEntity;
import com.buddyapp.paymybuddy.models.Contact;
import com.buddyapp.paymybuddy.models.MyContact;
import com.buddyapp.paymybuddy.models.MyTransaction;
import com.buddyapp.paymybuddy.models.Transaction;
import com.buddyapp.paymybuddy.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomMappers {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    TransactionRepository transactionRepository;


    public MyContact contactToMyContact(Contact contact) {
        if ( contact == null ) {
            return null;
        }

        MyContact myContact = new MyContact();
        ContactEntity contactEntityFromRep = contactRepository.findById(contact.getContactId()).get();
        myContact.setFirstname(contactEntityFromRep.getFriend().getFirstName());
        myContact.setLastname(contactEntityFromRep.getFriend().getLastName());
        myContact.setUsername(contactEntityFromRep.getFriend().getLogin());
        myContact.setMail(contactEntityFromRep.getFriend().getEmail());
        myContact.setIdentifier(contactEntityFromRep.getFriend().getUserName());


        return myContact;
    }

    public List<MyContact> contactsToMyContacts(List<Contact> contacts) {
        if ( contacts == null ) {
            return null;
        }

        List<MyContact> list = new ArrayList<MyContact>( contacts.size() );
        for ( Contact contact : contacts ) {
            list.add( contactToMyContact( contact ) );
        }

        return list;
    }


    public List<MyTransaction> transactionsToMyTransactions(List<Transaction> transactions) {
        if ( transactions == null ) {
            return null;
        }

        List<MyTransaction> list = new ArrayList<MyTransaction>( transactions.size() );
        for ( Transaction transaction : transactions ) {
            list.add( transactionToMyTransaction( transaction ) );
        }

        return list;
    }

    protected MyTransaction transactionToMyTransaction(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        MyTransaction myTransaction = new MyTransaction();

        if ( transaction.getAmount() != null ) {
            myTransaction.setAmount( transaction.getAmount() );
        }
        if ( transaction.getFee() != null ) {
            myTransaction.setFee( transaction.getFee() );
        }
        TransactionEntity transactionEntity = transactionRepository.findById(transaction.getTransactionId()).get();

        myTransaction.setDate( transaction.getDate() );
        myTransaction.setDescription( transaction.getDescription() );
        myTransaction.setAmount(transaction.getAmount());
        myTransaction.setReceiverUsername(transactionEntity.getTrader().getLogin());
        myTransaction.setSenderUsername(transactionEntity.getUser().getLogin());

        return myTransaction;
    }


}
