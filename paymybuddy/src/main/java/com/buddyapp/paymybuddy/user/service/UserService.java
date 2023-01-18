package com.buddyapp.paymybuddy.user.service;

import com.buddyapp.paymybuddy.DTOs.ContactDTO;
import com.buddyapp.paymybuddy.DTOs.RegistrationDTO;
import com.buddyapp.paymybuddy.DTOs.UserDTO;
import com.buddyapp.paymybuddy.constants.Provider;
import com.buddyapp.paymybuddy.entities.ContactEntity;
import com.buddyapp.paymybuddy.entities.TransactionEntity;
import com.buddyapp.paymybuddy.entities.UserEntity;
import com.buddyapp.paymybuddy.exception.ExceptionHandler;
import com.buddyapp.paymybuddy.helper.CycleAvoidingMappingContext;
import com.buddyapp.paymybuddy.mappers.UserMapper;
import com.buddyapp.paymybuddy.models.Contact;
import com.buddyapp.paymybuddy.models.MyUser;
import com.buddyapp.paymybuddy.models.Transaction;
import com.buddyapp.paymybuddy.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
public class UserService implements UserDetailsService, OAuth2UserService {


    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException("No user with that username"));
        MyUser myUser = userMapper.entityToModel(userEntity);
        List<SimpleGrantedAuthority> authi = new ArrayList<>();
        authi.add(new SimpleGrantedAuthority(myUser.getRoles()));
        // TODO add here a list of role if needed to do authorization
        return new org.springframework.security.core.userdetails.User(myUser.getUserName(), myUser.getPassword(), authi);
    }

    public List<MyUser> getUsers() {
        try {
            List<UserEntity> users = userRepository.findAll();
            return userMapper.entitiesToModel(users);
        } catch (Exception e) {
            log.error("Couldn't find all user: " + e.getMessage());
            throw new ExceptionHandler("We could not find your users");

        }
    }

    public MyUser createUser(UserDTO dto) {
        try {
            MyUser myUser = userMapper.dtoToModel(dto);
            myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
            myUser.setProvider(String.valueOf(Provider.LOCAL));
            userRepository.save(userMapper.modelToEntity(myUser));
            myUser = userMapper.entityToModel(userRepository.findByUserName(myUser.getUserName()).get());
            return myUser;
        } catch (Exception e) {
            log.error("Couldn't create user: " + e.getMessage());
            throw new ExceptionHandler("We could not create your account");
        }
    }

    public MyUser createOauth2User (UserDTO dto, Provider provider){
        try {
            MyUser myUser = userMapper.dtoToModel(dto);
            myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
            myUser.setProvider(String.valueOf(provider));
            userRepository.save(userMapper.modelToEntity(myUser));
            myUser = userMapper.entityToModel(userRepository.findByUserName(myUser.getUserName()).get());
            return myUser;
        } catch (Exception e) {
            log.error("Couldn't create user: " + e.getMessage());
            throw new ExceptionHandler("We could not create your account");
        }
    }

    /*
    public User updateUser(UserDTO dto) {
        try {
            User user = userMapper.dtoToModel(dto);
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            UserEntity userEntity = userRepository.findById(user.getUserId()).orElseThrow(()
                    -> new ExceptionHandler("We could not find your account"));
            userMapper.updateUserFromModel(user, userEntity, new CycleAvoidingMappingContext());
            userRepository.save(userEntity);
            return user;
        } catch (Exception e) {
            log.error("Couldn't update user: " + e.getMessage());
            throw new ExceptionHandler("We could not update your account");
        }
    }*/

    public MyUser getUserById(Long id) {
        try {
            return userMapper.entityToModel(userRepository.findById(id).orElseThrow(()
                    -> new ExceptionHandler("We could not find your account")));
        } catch (Exception e) {
            log.error("Couldn't find user: " + e.getMessage());
            throw new ExceptionHandler("We could not find your account");
        }
    }

    public MyUser getUserByEmail(String email) {
        try {
            return userMapper.entityToModel(userRepository.findByEmail(email).orElseThrow(()
                    -> new ExceptionHandler("We could not find your account")));
        } catch (Exception e) {
            log.error("Couldn't find user: " + e.getMessage());
            throw new ExceptionHandler("We could not find your account");
        }
    }

    public MyUser getUserByUserName(String userName) {
        try {
            return userMapper.entityToModel(userRepository.findByUserName(userName).orElseThrow(()
                    -> new ExceptionHandler("We could not find your account")));
        } catch (Exception e) {
            log.error("Couldn't find user: " + e.getMessage());
            throw new ExceptionHandler("We could not find your account");
        }
    }

    public String deleteUser(Long id) {
        try {
            UserEntity userEntity = userRepository.findById(id).orElseThrow(()
                    -> new ExceptionHandler("We could not find your account"));
            userRepository.delete(userEntity);
            return "Contact deleted";
        } catch (Exception e) {
            log.error("Couldn't delete user: " + e.getMessage());
            throw new ExceptionHandler("We could not delete your account");
        }
    }

    public void insertFundIntoApp(Double amount, Long userId) {
        try {
            UserEntity userEntity = userRepository.findById(userId).orElseThrow(()
                    -> new ExceptionHandler("We could not find your account"));
            MyUser myUser = userMapper.entityToModel(userEntity);
            myUser.setBalance(myUser.getBalance() + amount);
            userRepository.save(userMapper.modelToEntity(myUser));
        } catch (Exception e) {
            log.error("Couldn't find insertFundIntoApp: " + e.getMessage());
            throw new ExceptionHandler("We could not put your fund in your account");
        }
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return null;
    }

    public MyUser updateUser(UserDTO dto) {
        try {
            MyUser myUser = userMapper.entityToModel(userRepository.findByUserName(dto.getUserName()).orElseThrow(()
                    -> new ExceptionHandler("We could not find your user")));
            userMapper.updateUserFromDto(dto, myUser, new CycleAvoidingMappingContext());
            userRepository.save(userMapper.modelToEntity(myUser));
            return myUser;
        } catch (Exception e) {
            log.error("Couldn't create user: " + e.getMessage());
            throw new ExceptionHandler("We could not create your user");
        }
    }

    public String registerUser(RegistrationDTO registration){
        UserDTO newUser = new UserDTO(registration.getMail(), registration.getUsername(), registration.getPassword(),
                registration.getFirstname(), registration.getLastname(),null, "ROLE_USER", 0., new ArrayList<Contact>(), new ArrayList<Transaction>());
        MyUser newMe = createUser(newUser);
        if(newMe.getUserName().contentEquals(registration.getUsername())){
            return "Registered";
        }
        return "Username issue while registering";
    }

}
