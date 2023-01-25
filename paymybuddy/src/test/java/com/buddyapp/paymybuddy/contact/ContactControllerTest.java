package com.buddyapp.paymybuddy.contact;

import com.buddyapp.paymybuddy.DTOs.AddContactByMailDTO;
import com.buddyapp.paymybuddy.DTOs.AddContactByNameDTO;
import com.buddyapp.paymybuddy.DTOs.ContactDTO;
import com.buddyapp.paymybuddy.constants.Provider;
import com.buddyapp.paymybuddy.contact.repository.ContactRepository;
import com.buddyapp.paymybuddy.contact.service.ContactService;
import com.buddyapp.paymybuddy.entities.ContactEntity;
import com.buddyapp.paymybuddy.entities.TransactionEntity;
import com.buddyapp.paymybuddy.entities.UserEntity;
import com.buddyapp.paymybuddy.mappers.UserMapper;
import com.buddyapp.paymybuddy.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static com.buddyapp.paymybuddy.utils.ObjectAsJsonStrings.asJsonString;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = true)
public class ContactControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserMapper userMapper;


    private List<UserEntity> setupUsers() {
        UserEntity sender = userRepository.save(new UserEntity(3l, Provider.LOCAL, "mail1@mail.com", "user1",passwordEncoder.encode("pass1"), "firsteName1",
                "lastName1", "0101010101", "ROLE_USER", 1000., new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));
        UserEntity trader = userRepository.save(new UserEntity(4l, Provider.LOCAL, "mail2@mail.com", "user2",passwordEncoder.encode("pass2"), "firsteName2",
                "lastName2", "0202020202", "ROLE_USER", 0., new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));
        List<UserEntity> userList = new ArrayList<UserEntity>();
        userList.add(sender);
        userList.add(trader);

        return userList;
    }

    @Test
    public void getContactsAPI() throws Exception {

        List<UserEntity> userList = setupUsers();

        UserEntity sender = userList.get(0);

        UserEntity trader = userList.get(1);

        contactService.addContactByUsername(trader.getUserName(), userMapper.entityToModel(sender));

        String token = obtainAccessToken(sender.getUserName(), "pass1");

        AddContactByNameDTO addContactByNameDTO = new AddContactByNameDTO("user2");

        mockMvc.perform(get("/contact/contacts")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$[0].user").isNotEmpty())
                .andExpect(jsonPath("$[0].friend").isNotEmpty());
    }

    @Test
    public void getContactByIdAPI() throws Exception {

        List<UserEntity> userList = setupUsers();

        UserEntity sender = userList.get(0);

        UserEntity friend = userList.get(1);

        ContactEntity contact = new ContactEntity(1l, sender, friend);

        contactRepository.save(contact);

        String token = obtainAccessToken(sender.getUserName(), "pass1");

        mockMvc.perform(get("/contact/{id}", 1)
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$.contactId").value(1l))
                .andExpect(jsonPath("$.user.userName").value("user1"))
                .andExpect(jsonPath("$.friend.userName").value("user2"));
    }

    @Test
    public void createAPI() throws Exception {

        List<UserEntity> userList = setupUsers();

        UserEntity sender = userList.get(0);

        UserEntity friend = userList.get(1);

        ContactDTO contactDTO = new ContactDTO(1l, userMapper.entityToModel(sender), userMapper.entityToModel(friend));

        String token = obtainAccessToken(sender.getUserName(), "pass1");

        mockMvc.perform(put("/contact/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(contactDTO)))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$.contactId").value(1l))
                .andExpect(jsonPath("$.user.userName").value("user1"))
                .andExpect(jsonPath("$.friend.userName").value("user2"));
    }

    @Test
    public void updateAPI() throws Exception {

        List<UserEntity> userList = setupUsers();

        UserEntity sender = userList.get(0);

        UserEntity friend = userList.get(1);

        ContactDTO contactDTO = new ContactDTO(1l, userMapper.entityToModel(sender), userMapper.entityToModel(sender));

        ContactEntity contact = new ContactEntity(1l, sender, friend);
        contactRepository.save(contact);

        String token = obtainAccessToken(sender.getUserName(), "pass1");

        mockMvc.perform(post("/contact/update")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(contactDTO)))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$.contactId").value(1l))
                .andExpect(jsonPath("$.user.userName").value("user1"))
                .andExpect(jsonPath("$.friend.userName").value("user1"));
    }

    @Test
    public void deleteAPI() throws Exception {

        List<UserEntity> userList = setupUsers();

        UserEntity sender = userList.get(0);

        UserEntity friend = userList.get(1);

        ContactEntity contact = new ContactEntity(1l, sender, friend);

        contactRepository.save(contact);

        String token = obtainAccessToken(sender.getUserName(), "pass1");

        mockMvc.perform(delete("/contact/{id}", 1)
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Contact deleted")));

    }

    @Test
    public void addContactByUsernameAPI() throws Exception {

        UserEntity sender = userRepository.save(new UserEntity(3l, Provider.LOCAL, "mail1@mail.com", "user1",passwordEncoder.encode("pass1"), "firsteName1",
                "lastName1", "0101010101", "ROLE_USER", 1000., new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));
        UserEntity trader = userRepository.save(new UserEntity(4l, Provider.LOCAL, "mail2@mail.com", "user2",passwordEncoder.encode("pass2"), "firsteName2",
                "lastName2", "0202020202", "ROLE_USER", 0., new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));

        AddContactByNameDTO addContactByNameDTO = new AddContactByNameDTO("user2");

        String token = obtainAccessToken(sender.getUserName(), "pass1");


        mockMvc.perform(put("/contact/addcontactbyusername")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addContactByNameDTO)))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$.myContacts[0].mail").value("mail2@mail.com"));

    }

    @Test
    public void addContactByMailAPI() throws Exception {

        UserEntity sender = userRepository.save(new UserEntity(3l, Provider.LOCAL, "mail1@mail.com", "user1",passwordEncoder.encode("pass1"), "firsteName1",
                "lastName1", "0101010101", "ROLE_USER", 1000., new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));
        UserEntity trader = userRepository.save(new UserEntity(4l, Provider.LOCAL, "mail2@mail.com", "user2",passwordEncoder.encode("pass2"), "firsteName2",
                "lastName2", "0202020202", "ROLE_USER", 0., new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));

        AddContactByMailDTO addContactByMailDTO = new AddContactByMailDTO("mail2@mail.com");

        String token = obtainAccessToken(sender.getUserName(), "pass1");


        mockMvc.perform(put("/contact/addcontactbymail")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addContactByMailDTO)))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$.myContacts[0].username").value("user2"));

    }

    @Test
    public void removeContactByUsernameAPI() throws Exception {
        UserEntity sender = userRepository.save(new UserEntity(3l, Provider.LOCAL, "mail1@mail.com", "user1",passwordEncoder.encode("pass1"), "firsteName1",
                "lastName1", "0101010101", "ROLE_USER", 1000., new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));
        UserEntity trader = userRepository.save(new UserEntity(4l, Provider.LOCAL, "mail2@mail.com", "user2",passwordEncoder.encode("pass2"), "firsteName2",
                "lastName2", "0202020202", "ROLE_USER", 0., new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));

        contactService.addContactByUsername("user1", userMapper.entityToModel(trader));

        String token = obtainAccessToken(trader.getUserName(), "pass2");

        AddContactByNameDTO addContactByNameDTO = new AddContactByNameDTO("user1");

        mockMvc.perform(delete("/contact/removecontactbyusername")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addContactByNameDTO)))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$.myContacts").isEmpty());

    }

    @Test
    public void  myContactsAPI() throws Exception {
        UserEntity sender = userRepository.save(new UserEntity(3l, Provider.LOCAL, "mail1@mail.com", "user1",passwordEncoder.encode("pass1"), "firsteName1",
                "lastName1", "0101010101", "ROLE_USER", 1000., new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));
        UserEntity trader = userRepository.save(new UserEntity(4l, Provider.LOCAL, "mail2@mail.com", "user2",passwordEncoder.encode("pass2"), "firsteName2",
                "lastName2", "0202020202", "ROLE_USER", 0., new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));

        contactService.addContactByUsername("user2", userMapper.entityToModel(sender));

        String token = obtainAccessToken(sender.getUserName(), "pass1");

        AddContactByNameDTO addContactByNameDTO = new AddContactByNameDTO("user2");

        mockMvc.perform(get("/contact/mycontacts")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$.myContacts").isArray());
    }

    public String obtainAccessToken(String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "userName");
        params.add("username", username);
        params.add("password", password);

        ResultActions result
                = mockMvc.perform(post("/token")
                        .params(params)
                        .with(httpBasic(username,password)))
                .andExpect(status().isOk());

        return result.andReturn().getResponse().getHeader("Token");
    }




}
