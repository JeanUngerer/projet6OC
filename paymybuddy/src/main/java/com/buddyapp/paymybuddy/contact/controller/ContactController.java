package com.buddyapp.paymybuddy.contact.controller;

import com.buddyapp.paymybuddy.DTOs.ContactDTO;
import com.buddyapp.paymybuddy.contact.service.ContactService;
import com.buddyapp.paymybuddy.mappers.ContactMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequestMapping("contact")
public class ContactController {

    ContactService contactService;

    private ContactMapper contactMapper;

    @GetMapping("/contacts")
    public ResponseEntity<List<ContactDTO>> getContacts() {
        return ResponseEntity.ok(contactMapper.modelsToDtos(contactService.findAllContact()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(contactMapper.modelToDto(contactService.findContactById(id)));
    }

    @PutMapping("/create")
    public ResponseEntity<ContactDTO> createUser(@RequestBody ContactDTO contactDto) {
        return ResponseEntity.ok(contactMapper.modelToDto(contactService.createContact(contactDto)));
    }

    @PostMapping("")
    public ResponseEntity<ContactDTO> updateUser(@RequestBody ContactDTO contactDto) {
        return ResponseEntity.ok(contactMapper.modelToDto(contactService.updateContact(contactDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(contactService.deleteContact(id));
    }


}
