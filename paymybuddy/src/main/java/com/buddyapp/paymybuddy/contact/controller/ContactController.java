package com.buddyapp.paymybuddy.contact.controller;

import com.buddyapp.paymybuddy.DTOs.AddContactByMailDTO;
import com.buddyapp.paymybuddy.DTOs.AddContactByNameDTO;
import com.buddyapp.paymybuddy.DTOs.ContactDTO;
import com.buddyapp.paymybuddy.DTOs.MyContactsDTO;
import com.buddyapp.paymybuddy.auth.service.TokenService;
import com.buddyapp.paymybuddy.contact.service.ContactService;
import com.buddyapp.paymybuddy.mappers.ContactMapper;
import com.buddyapp.paymybuddy.models.MyUser;
import com.buddyapp.paymybuddy.user.service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequestMapping("contact")
public class ContactController {

    @Autowired
    ContactService contactService;
    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    private ContactMapper contactMapper;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Full CRUD for admin
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/contacts")
    public ResponseEntity<List<ContactDTO>> getContacts() {
        return ResponseEntity.ok(contactMapper.modelsToDtos(contactService.findAllContact()));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(contactMapper.modelToDto(contactService.findContactById(id)));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PutMapping("/create")
    public ResponseEntity<ContactDTO> create(@RequestBody ContactDTO contactDto) {
        return ResponseEntity.ok(contactMapper.modelToDto(contactService.createContact(contactDto)));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<ContactDTO> update(@RequestBody ContactDTO contactDto) {
        return ResponseEntity.ok(contactMapper.modelToDto(contactService.updateContact(contactDto)));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok(contactService.deleteContact(id));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // User accessible
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN', 'SCOPE_OAUTH2_USER')")
    @GetMapping("/mycontacts")
    public ResponseEntity<MyContactsDTO> myContacts(@RequestHeader("Authorization") String requestTokenHeader){
        MyUser me = userService.getUserByUserName(tokenService.decodeTokenUsername(requestTokenHeader));
        return ResponseEntity.ok(contactService.getMyContacts(me));
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN', 'SCOPE_OAUTH2_USER')")
    @PutMapping("/addcontactbymail")
    public ResponseEntity<MyContactsDTO> AddContactByMail(@RequestHeader("Authorization") String requestTokenHeader, @RequestBody AddContactByMailDTO dto){
        MyUser me = userService.getUserByUserName(tokenService.decodeTokenUsername(requestTokenHeader));
        contactService.addContactByMail(dto.getMail(), me);
        return ResponseEntity.ok(contactService.getMyContacts(userService.getUserByUserName(me.getUserName())));
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN', 'SCOPE_OAUTH2_USER')")
    @PutMapping("/addcontactbyusername")
    public ResponseEntity<MyContactsDTO> addContactByUsername(@RequestHeader("Authorization") String requestTokenHeader, @RequestBody AddContactByNameDTO dto){
        MyUser me = userService.getUserByUserName(tokenService.decodeTokenUsername(requestTokenHeader));
        contactService.addContactByUsername(dto.getUsername(), me);
        return ResponseEntity.ok(contactService.getMyContacts(userService.getUserByUserName(me.getUserName())));
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN', 'SCOPE_OAUTH2_USER')")
    @DeleteMapping("/removecontactbyusername")
    public ResponseEntity<MyContactsDTO> removeContact(@RequestHeader("Authorization") String requestTokenHeader, @RequestBody AddContactByNameDTO dto){
        MyUser me = userService.getUserByUserName(tokenService.decodeTokenUsername(requestTokenHeader));
        contactService.removeContactByUsername(dto.getUsername(), me);
        return ResponseEntity.ok(contactService.getMyContacts(userService.getUserByUserName(me.getUserName())));
    }


}
