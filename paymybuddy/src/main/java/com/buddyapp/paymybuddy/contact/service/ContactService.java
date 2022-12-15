package com.buddyapp.paymybuddy.contact.service;


import com.buddyapp.paymybuddy.DTOs.ContactDTO;
import com.buddyapp.paymybuddy.contact.repository.ContactRepository;
import com.buddyapp.paymybuddy.exception.ExceptionHandler;
import com.buddyapp.paymybuddy.helper.CycleAvoidingMappingContext;
import com.buddyapp.paymybuddy.mappers.ContactMapper;
import com.buddyapp.paymybuddy.models.Contact;
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
    ContactMapper contactMapper;

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
}
