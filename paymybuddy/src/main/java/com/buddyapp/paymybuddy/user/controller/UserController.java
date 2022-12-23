package com.buddyapp.paymybuddy.user.controller;

import com.buddyapp.paymybuddy.DTOs.UserDTO;
import com.buddyapp.paymybuddy.mappers.UserMapper;
import com.buddyapp.paymybuddy.user.service.UserService;
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
@RequestMapping("user")
public class UserController {

    UserService userService;

    private UserMapper userMapper;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(userMapper.modelsToDtos(userService.getUsers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUsers(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userMapper.modelToDto(userService.getUserById(id)));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByMail(@PathVariable("email") String email) {
        return ResponseEntity.ok(userMapper.modelToDto(userService.getUserByEmail(email)));
    }

    @PutMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user) {
        return ResponseEntity.ok(userMapper.modelToDto(userService.createUser(user)));
    }

    /*
    @PostMapping("")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO user) {
        return ResponseEntity.ok(userMapper.modelToDto(userService.updateUser(user)));
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }


}
