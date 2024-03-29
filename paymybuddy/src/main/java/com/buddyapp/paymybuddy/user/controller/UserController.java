package com.buddyapp.paymybuddy.user.controller;

import com.buddyapp.paymybuddy.DTOs.*;
import com.buddyapp.paymybuddy.auth.service.TokenService;
import com.buddyapp.paymybuddy.mappers.UserMapper;
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
@RequestMapping("user")
public class UserController {

    UserService userService;

    private UserMapper userMapper;

    @Autowired
    TokenService tokenService;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Full CRUD for admin
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(userMapper.modelsToDtos(userService.getUsers()));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUsers(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userMapper.modelToDto(userService.getUserById(id)));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByMail(@PathVariable("email") String email) {
        return ResponseEntity.ok(userMapper.modelToDto(userService.getUserByEmail(email)));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PutMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user) {
        return ResponseEntity.ok(userMapper.modelToDto(userService.createUser(user)));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO user) {
        return ResponseEntity.ok(userMapper.modelToDto(userService.updateUser(user)));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // User accessible
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN', 'SCOPE_OAUTH2_USER')")
    @GetMapping("/mybalance")
    public ResponseEntity<MyBalanceDTO> getMyBalance(@RequestHeader("Authorization") String requestTokenHeader){
        MyUser me = userService.getUserByUserName(tokenService.decodeTokenUsername(requestTokenHeader));
        return ResponseEntity.ok( new MyBalanceDTO(me.getBalance()) );
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN', 'SCOPE_OAUTH2_USER')")
    @PutMapping("/addfunds")
    public ResponseEntity<MyBalanceDTO> addFunds(@RequestHeader("Authorization") String requestTokenHeader, @RequestBody AddFundsDTO dto){
        MyUser me = userService.getUserByUserName(tokenService.decodeTokenUsername(requestTokenHeader));

        return ResponseEntity.ok( new MyBalanceDTO(userService.insertFundIntoApp(dto.getAmount(), me.getUserId())) );
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN', 'SCOPE_OAUTH2_USER')")
    @PutMapping("/withdrawfunds")
    public ResponseEntity<MyBalanceDTO> withdrawFunds(@RequestHeader("Authorization") String requestTokenHeader, @RequestBody AddFundsDTO dto){
        MyUser me = userService.getUserByUserName(tokenService.decodeTokenUsername(requestTokenHeader));

        return ResponseEntity.ok( new MyBalanceDTO(userService.withdrawFundFromApp(dto.getAmount(), me.getUserId())) );
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN', 'SCOPE_OAUTH2_USER')")
    @PutMapping("/phone")
    public ResponseEntity<ProfileModificationDTO> setMyPhone(@RequestHeader("Authorization") String requestTokenHeader, @RequestBody ProfileModificationDTO dto){
        MyUser me = userService.getUserByUserName(tokenService.decodeTokenUsername(requestTokenHeader));
        me.setPhoneNumber(dto.getModification());
        me = userService.updateUser(userMapper.modelToDto(me));
        return ResponseEntity.ok(new ProfileModificationDTO(me.getPhoneNumber()));
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN', 'SCOPE_OAUTH2_USER')")
    @GetMapping("/phone")
    public ResponseEntity<ProfileModificationDTO> getMyPhone(@RequestHeader("Authorization") String requestTokenHeader){
        MyUser me = userService.getUserByUserName(tokenService.decodeTokenUsername(requestTokenHeader));

        return ResponseEntity.ok( new ProfileModificationDTO(me.getPhoneNumber()));
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN', 'SCOPE_OAUTH2_USER')")
    @GetMapping("/fname")
    public ResponseEntity<ProfileModificationDTO> getMyFirstname(@RequestHeader("Authorization") String requestTokenHeader){
        MyUser me = userService.getUserByUserName(tokenService.decodeTokenUsername(requestTokenHeader));

        return ResponseEntity.ok( new ProfileModificationDTO(me.getFirstName()));
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN', 'SCOPE_OAUTH2_USER')")
    @PutMapping("/fname")
    public ResponseEntity<ProfileModificationDTO> setMyFirstname(@RequestHeader("Authorization") String requestTokenHeader, @RequestBody ProfileModificationDTO dto){
        MyUser me = userService.getUserByUserName(tokenService.decodeTokenUsername(requestTokenHeader));
        me.setFirstName(dto.getModification());
        me = userService.updateUser(userMapper.modelToDto(me));
        return ResponseEntity.ok(new ProfileModificationDTO(me.getPhoneNumber()));
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN', 'SCOPE_OAUTH2_USER')")
    @GetMapping("/lname")
    public ResponseEntity<ProfileModificationDTO> getMyLastname(@RequestHeader("Authorization") String requestTokenHeader){
        MyUser me = userService.getUserByUserName(tokenService.decodeTokenUsername(requestTokenHeader));

        return ResponseEntity.ok( new ProfileModificationDTO(me.getLastName()));
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN', 'SCOPE_OAUTH2_USER')")
    @PutMapping("/lname")
    public ResponseEntity<ProfileModificationDTO> setMyLastname(@RequestHeader("Authorization") String requestTokenHeader, @RequestBody ProfileModificationDTO dto){
        MyUser me = userService.getUserByUserName(tokenService.decodeTokenUsername(requestTokenHeader));
        me.setLastName(dto.getModification());
        me = userService.updateUser(userMapper.modelToDto(me));
        return ResponseEntity.ok(new ProfileModificationDTO(me.getPhoneNumber()));
    }
}
