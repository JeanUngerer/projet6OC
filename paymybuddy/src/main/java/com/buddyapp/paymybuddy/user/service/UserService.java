package com.buddyapp.paymybuddy.user.service;

import com.buddyapp.paymybuddy.DTOs.UserDTO;
import com.buddyapp.paymybuddy.entities.UserEntity;
import com.buddyapp.paymybuddy.exception.ExceptionHandler;
import com.buddyapp.paymybuddy.mappers.UserMapper;
import com.buddyapp.paymybuddy.models.MyUser;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
public class UserService implements UserDetailsService, OAuth2UserService {

    @Autowired
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No user with that email"));
        MyUser myUser = userMapper.entityToModel(userEntity);
        List<SimpleGrantedAuthority> authi = new ArrayList<>();
        authi.add(new SimpleGrantedAuthority(myUser.getRoles()));
        // TODO add here a list of role if needed to do authorization
        return new org.springframework.security.core.userdetails.User(myUser.getEmail(), myUser.getPassword(), authi);
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
            userRepository.save(userMapper.modelToEntity(myUser));
            myUser = userMapper.entityToModel(userRepository.findByEmail(myUser.getEmail()).get());
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

//    public String getRoleAccordingToJWT(JwtChecks JWT) {
//
//        if (JWT.getToken() != null && JWT.getToken().startsWith("Bearer ")) {
//
//            String token = JWT.getToken().substring("Bearer ".length());
//            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
//            JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm).build();
//            DecodedJWT decodedJWT = verifier.verify(token);
//            String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
//            return roles[0];
//        } else {
//            return "authNeeded";
//        }
//    }
}
