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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.util.*;

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
        return new org.springframework.security.core.userdetails.User(myUser.getUserName(), myUser.getPassword(), authi);
    }



    public List<MyUser> getUsers() {
        try {
            log.info("getUsers");
            List<UserEntity> users = userRepository.findAll();
            return userMapper.entitiesToModel(users);
        } catch (Exception e) {
            log.error("Couldn't find all user: " + e.getMessage());
            throw new ExceptionHandler("We could not find your users");

        }
    }

    public MyUser createUser(UserDTO dto) {
        try {
            log.info("create user - userDTO: " + dto.toString());
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
            log.info("createOauth2User - userDTO: " + dto.toString() + " - provider: " + provider);
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

    public MyUser getUserById(Long id) {
        try {
            log.info("getUserById - id: " + id.toString());
            return userMapper.entityToModel(userRepository.findById(id).orElseThrow(()
                    -> new ExceptionHandler("We could not find your account")));
        } catch (Exception e) {
            log.error("Couldn't find user: " + e.getMessage());
            throw new ExceptionHandler("We could not find your account");
        }
    }

    public MyUser getUserByEmail(String email) {
        try {
            log.info("getUserByEmail - email: " + email);
            return userMapper.entityToModel(userRepository.findByEmail(email).orElseThrow(()
                    -> new ExceptionHandler("We could not find your account")));
        } catch (Exception e) {
            log.error("Couldn't find user: " + e.getMessage());
            throw new ExceptionHandler("We could not find your account");
        }
    }

    public MyUser getUserByUserName(String userName) {
        try {
            log.info("getUserByUserName - userName: " + userName);
            MyUser mee =userMapper.entityToModel(userRepository.findByUserName(userName).orElseThrow(()
                    -> new ExceptionHandler("We could not find your account")));
            return userMapper.entityToModel(userRepository.findByUserName(userName).orElseThrow(()
                    -> new ExceptionHandler("We could not find your account")));
        } catch (Exception e) {
            log.error("Couldn't find user: " + e.getMessage());
            throw new ExceptionHandler("We could not find your account");
        }
    }

    public String deleteUser(Long id) {
        try {
            log.info("deleteUser - id: " + id.toString());
            UserEntity userEntity = userRepository.findById(id).orElseThrow(()
                    -> new ExceptionHandler("We could not find your account"));
            userRepository.delete(userEntity);
            return "Contact deleted";
        } catch (Exception e) {
            log.error("Couldn't delete user: " + e.getMessage());
            throw new ExceptionHandler("We could not delete your account");
        }
    }

    public double insertFundIntoApp(double amount, Long userId) {
        try {
            log.info("insertFundIntoApp - userId: " + userId.toString() + " - amount: " + amount);
            UserEntity userEntity = userRepository.findById(userId).orElseThrow(()
                    -> new ExceptionHandler("We could not find your account"));
            userEntity.setBalance(userEntity.getBalance() + amount);
            userRepository.save(userEntity);
            return userEntity.getBalance();
        } catch (Exception e) {
            log.error("insertFundIntoApp: " + e.getMessage());
            throw new ExceptionHandler("We could not put your fund in your account");
        }
    }

    public double withdrawFundFromApp(double amount, Long userId) {
        try {
            log.info("withdrawFundFromApp - userId: " + userId.toString() + " - amount: " + amount);
            UserEntity userEntity = userRepository.findById(userId).orElseThrow(()
                    -> new ExceptionHandler("We could not find your account"));
            if(userEntity.getBalance() < amount) {throw new ExceptionHandler("Not enough");}
            userEntity.setBalance(userEntity.getBalance() - amount);
            userRepository.save(userEntity);
            return userEntity.getBalance();
        } catch (Exception e) {
            log.error("withdrawFundFromApp: " + e.getMessage());
            throw new ExceptionHandler("We could not withdraw your funds from your account");
        }
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return null;
    }

    public MyUser updateUser(UserDTO dto) {
        try {
            log.info("updateUser - UserDTO: " + dto.toString());
            MyUser myUser = userMapper.entityToModel(userRepository.findByUserName(dto.getUserName()).orElseThrow(()
                    -> new ExceptionHandler("We could not find your user")));
            userMapper.updateUserFromDto(dto, myUser, new CycleAvoidingMappingContext());
            userRepository.save(userMapper.modelToEntity(myUser));
            return myUser;
        } catch (Exception e) {
            log.error("Couldn't update user: " + e.getMessage());
            throw new ExceptionHandler("We could not update your user");
        }
    }

    public String registerUser(RegistrationDTO registration){
        try {
            log.info("registerUser - RegistrationDTO: " + registration.toString());
            UserDTO newUser = new UserDTO(registration.getMail(), registration.getUsername(), registration.getUsername(), registration.getPassword(),
                    registration.getFirstname(), registration.getLastname(), null, "ROLE_USER", 0., new ArrayList<Contact>(), new ArrayList<Transaction>());
            MyUser newMe = createUser(newUser);
            if (newMe.getUserName().contentEquals(registration.getUsername())) {
                return "Registered";
            }
            return "Username issue while registering";
        } catch (Exception e) {
            log.error("registerUser: " + e.getMessage());
            throw new ExceptionHandler("We could not register your user");
        }
    }
}
