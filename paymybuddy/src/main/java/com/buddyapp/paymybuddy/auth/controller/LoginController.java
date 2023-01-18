package com.buddyapp.paymybuddy.auth.controller;


import com.buddyapp.paymybuddy.DTOs.MessageDTO;
import com.buddyapp.paymybuddy.DTOs.RegistrationDTO;
import com.buddyapp.paymybuddy.auth.service.TokenService;
import com.buddyapp.paymybuddy.user.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LoginController {

    private final TokenService tokenService;

    private final UserService userService;

    public LoginController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("/token")
    public ResponseEntity<Void> token(Authentication authentication) {
        log.debug("Token requested for user : " + authentication.getName());
        String token = tokenService.generateToken(authentication);
        log.debug("Token granted : " + token);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Token", token);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .build();
    }



    @GetMapping("/*")
    public ResponseEntity<MessageDTO> home(){
        return ResponseEntity.ok(new MessageDTO("Hi AUthenticated !"));
    }

    @GetMapping("/home")
    public ResponseEntity<MessageDTO> homeSweetHome(){
        return ResponseEntity.ok(new MessageDTO("Hi AUthenticated !"));
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN', 'SCOPE_OAUTH2_USER')")
    @RequestMapping("/user")
    public ResponseEntity<MessageDTO> getUser(@RequestHeader("Authorization") String requestTokenHeader) {
        log.info("User");
        return ResponseEntity.ok(new MessageDTO("Hi " + tokenService.decodeTokenUsername(requestTokenHeader)));

    }

    @PutMapping("/register")
    public ResponseEntity<MessageDTO> register(@RequestBody RegistrationDTO dto){
        String message = userService.registerUser(dto);
        return ResponseEntity.ok(new MessageDTO(message));
    }

    @RequestMapping("/authi")
    public ResponseEntity<MessageDTO> getAuthi() {
        log.info("Authi");
        return ResponseEntity.ok(new MessageDTO("Not AUthenticated !"));
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @RequestMapping("/admin")
    public ResponseEntity<MessageDTO> getAdmin() {
        log.info("ADMIN");
        return ResponseEntity.ok(new MessageDTO("Hi Admin !"));
    }

    @RequestMapping("/github/*")
    public ResponseEntity<MessageDTO> getGithub()
    {
        return ResponseEntity.ok(new MessageDTO("Hi Github !"));
    }
}
