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


import java.util.ArrayList;
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
            log.info("findAllContact");
            List<Contact> contactList = new ArrayList<Contact>();
            contactRepository.findAll().forEach(ct -> contactList.add(contactMapper.entityToModel(ct)));
            return  contactList;
        } catch (Exception e) {
            log.error("We could not find all contact: " + e.getMessage());
            throw new ExceptionHandler("We could not find your contacts");
        }
    }

    public Contact findContactById(Long id) {
        try {
            log.info("findContactById - id: " + id.toString());
            Contact contact = contactMapper.entityToModel(contactRepository.findById(id).orElseThrow(()
                    -> new ExceptionHandler("We didn't find your contact")));
            return contact;
        } catch (Exception e) {
            log.error("We could not find all contact: " + e.getMessage());
            throw new ExceptionHandler("We could not find your contact");
        }
    }

    public Contact createContact(ContactDTO dto) {
        try {
            log.info("createContact");
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
            log.info("updateContact - id: " + dto.getContactId().toString());
            Contact contact = contactMapper.entityToModel(contactRepository.findById(dto.getContactId()).orElseThrow(()
                    -> new ExceptionHandler("We could not find your contact")));
            contactMapper.updateContactFromDto(dto, contact, new CycleAvoidingMappingContext());
            contactRepository.save(contactMapper.modelToEntity(contact));
            return contact;
        } catch (Exception e) {
            log.error("Couldn't update user: " + e.getMessage());
            throw new ExceptionHandler("We could not update your contact");
        }
    }

    public String deleteContact(Long id) {
        try {
            log.info("deleteContact - id: " + id.toString());
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
        try {
            log.info("addContactByMail - " + me.getUserName());
            ContactDTO newContact = new ContactDTO(null, me, userService.getUserByEmail(mailAddress));
            createContact(newContact);
        } catch (Exception e) {
            log.error("Could not add contact: " + e.getMessage());
        }
    }

    public MyContactsDTO getMyContacts(MyUser me) {
        try {
            log.info("getMyContacts - " + me.getUserName());
            MyContactsDTO myContactsDTO = new MyContactsDTO();
            myContactsDTO.setMyContacts(customMapper.contactsToMyContacts(contactMapper.entitiesToModels(contactRepository.findAllByUser_UserId(me.getUserId()).get())));

            return myContactsDTO;
        } catch (Exception e) {
            log.error("Could not add contact: " + e.getMessage());
            throw new ExceptionHandler("We could not add your contact");
        }
    }

    private Contact findContactByUserAndFriend(MyUser me, MyUser friend){
        try {
            Contact contact = contactMapper.entityToModel(contactRepository.findByUserAndFriend(userMapper.modelToEntity(me), userMapper.modelToEntity(friend)).get());
            return contact;
        } catch (Exception e) {
            log.error("Could not find your contact to delete: " + e.getMessage());
            throw new ExceptionHandler("We could not find your contact to delete");
        }
    }

    public void addContactByUsername(String username, MyUser me) {
        try {
            log.info("addContactByUsername - " + me.getUserName());
            ContactDTO newContact = new ContactDTO(null, me, userService.getUserByUserName(username));
            createContact(newContact);
        } catch (Exception e) {
            log.error("Could not add your contact: " + e.getMessage());
            throw new ExceptionHandler("We could not add your contact");
        }
    }

    public void removeContactByUsername(String username, MyUser me) {
        try {
            log.info("removeContactByUsername - " + me.getUserName());
            MyUser friend = userService.getUserByUserName(username);
            deleteContact(findContactByUserAndFriend(me, friend).getContactId());
        } catch (Exception e) {
            log.error("Could not remove your contact: " + e.getMessage());
            throw new ExceptionHandler("We could not remove your contact");
        }
    }
}
