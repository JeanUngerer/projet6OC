package com.buddyapp.paymybuddy.contact.service;


import com.buddyapp.paymybuddy.DTOs.ContactDTO;
import com.buddyapp.paymybuddy.DTOs.MyContactsDTO;
import com.buddyapp.paymybuddy.contact.repository.ContactRepository;
import com.buddyapp.paymybuddy.entities.ContactEntity;
import com.buddyapp.paymybuddy.entities.UserEntity;
import com.buddyapp.paymybuddy.exception.ExceptionHandler;
import com.buddyapp.paymybuddy.helper.CycleAvoidingMappingContext;
import com.buddyapp.paymybuddy.mappers.ContactMapper;
import com.buddyapp.paymybuddy.mappers.CustomMappers;
import com.buddyapp.paymybuddy.mappers.UserMapper;
import com.buddyapp.paymybuddy.models.Contact;
import com.buddyapp.paymybuddy.models.MyUser;
import com.buddyapp.paymybuddy.user.repository.UserRepository;
import com.buddyapp.paymybuddy.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
@Transactional
public class ContactService {

    ContactRepository contactRepository;

    UserRepository userRepository;
    ContactMapper contactMapper;

    CustomMappers customMapper;

    UserService userService;

    UserMapper userMapper;

    public List<Contact> findAllContact() {
        try {
            return contactMapper.entitiesToModels(contactRepository.findAll());
        } catch (Exception e) {
            log.error("We could not find all contact: " + e.getMessage());
            throw new ExceptionHandler("We could not find your contacts");
        }
    }

    public Contact findContactById(Long id) {
        try {
            Contact contact = contactMapper.entityToModel(contactRepository.findById(id).orElseThrow(()
                    -> new ExceptionHandler("We didn't find your contact")));
            return contact;
        } catch (Exception e) {
            log.error("We could not find all contact: " + e.getMessage());
            throw new ExceptionHandler("We could not find your contacts");
        }
    }

    public Contact createContact(ContactDTO dto) {
        try {
            Contact contact = contactMapper.dtoToModel(dto);
            contactRepository.save(contactMapper.modelToEntity(contact));
            return contact;
        } catch (Exception e) {
            log.error("Couldn't contact user: " + e.getMessage());
            throw new ExceptionHandler("We could not create your contact");
        }
    }
    public Contact updateContact(ContactDTO dto) {
        try {
            Contact contact = contactMapper.entityToModel(contactRepository.findById(dto.getContactId()).orElseThrow(()
                    -> new ExceptionHandler("We could not find your contact")));
            contactMapper.updateContactFromDto(dto, contact, new CycleAvoidingMappingContext());
            contactRepository.save(contactMapper.modelToEntity(contact));
            return contact;
        } catch (Exception e) {
            log.error("Couldn't create user: " + e.getMessage());
            throw new ExceptionHandler("We could not create your contact");
        }
    }

    public String deleteContact(Long id) {
        try {
            Contact contact = contactMapper.entityToModel(contactRepository.findById(id).orElseThrow(()
                    -> new ExceptionHandler("We could not find your contact")));
            contactRepository.delete(contactMapper.modelToEntity(contact));
            return "Contact deleted";
        } catch (Exception e) {
            log.error("Couldn't delete contact: " + e.getMessage());
            throw new ExceptionHandler("We could not delete your contact");
        }
    }

    public void addContactByMail(String mailAddress, MyUser me) {
        ContactDTO newContact = new ContactDTO(null, me, userService.getUserByEmail(mailAddress));
        createContact(newContact);
    }

    public MyContactsDTO getMyContacts(MyUser me) {
        MyContactsDTO myContactsDTO = new MyContactsDTO();

        List<ContactEntity> list = contactRepository.findAllByUser_UserId(me.getUserId()).get();
        myContactsDTO.setMyContacts(customMapper.contactsToMyContacts(contactMapper.entitiesToModels(contactRepository.findAllByUser_UserId(me.getUserId()).get())));

        return myContactsDTO;
    }

    public Contact findContactByUserAndFriend(MyUser me, MyUser friend){
        Contact contact = contactMapper.entityToModel(contactRepository.findByUserAndFriend(userMapper.modelToEntity(me), userMapper.modelToEntity(friend)).get());
        return contact;
    }

    public void addContactByUsername(String username, MyUser me) {
        ContactDTO newContact = new ContactDTO(null, me, userService.getUserByUserName(username));
        createContact(newContact);
    }

    public void removeContactByUsername(String username, MyUser me) {
        MyUser friend = userService.getUserByUserName(username);
        deleteContact(findContactByUserAndFriend(me, friend).getContactId());
    }
}
