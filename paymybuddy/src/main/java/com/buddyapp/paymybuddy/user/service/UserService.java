package com.buddyapp.paymybuddy.user.service;

import com.buddyapp.paymybuddy.DTOs.UserDTO;
import com.buddyapp.paymybuddy.entities.UserEntity;
import com.buddyapp.paymybuddy.exception.ExceptionHandler;
import com.buddyapp.paymybuddy.helper.CycleAvoidingMappingContext;
import com.buddyapp.paymybuddy.mappers.UserMapper;
import com.buddyapp.paymybuddy.models.User;
import com.buddyapp.paymybuddy.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
public class UserService implements UserDetailsService {

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No user with that email"));
        User user = userMapper.entityToModel(userEntity);
        // TODO add here a list of role if needed to do authorization
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public List<User> getUsers() {
        try {
            List<UserEntity> users = userRepository.findAll();
            return userMapper.entitiesToModel(users);
        } catch (Exception e) {
            log.error("Couldn't find all user: " + e.getMessage());
            throw new ExceptionHandler("We could not find your users");

        }
    }

    public User createUser(UserDTO dto) {
        try {
            User user = userMapper.dtoToModel(dto);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(userMapper.modelToEntity(user));
            return user;
        } catch (Exception e) {
            log.error("Couldn't create user: " + e.getMessage());
            throw new ExceptionHandler("We could not create your account");
        }
    }

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
    }

    public User getUserById(Long id) {
        try {
            return userMapper.entityToModel(userRepository.findById(id).orElseThrow(()
                    -> new ExceptionHandler("We could not find your account")));
        } catch (Exception e) {
            log.error("Couldn't find user: " + e.getMessage());
            throw new ExceptionHandler("We could not find your account");
        }
    }

    public User getUserByEmail(String email) {
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
            User user = userMapper.entityToModel(userEntity);
            user.setBalance(user.getBalance() + amount);
            userRepository.save(userMapper.modelToEntity(user));
        } catch (Exception e) {
            log.error("Couldn't find insertFundIntoApp: " + e.getMessage());
            throw new ExceptionHandler("We could not put your fund in your account");
        }
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
