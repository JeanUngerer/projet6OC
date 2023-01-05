package com.buddyapp.paymybuddy.auth.controller;


import com.buddyapp.paymybuddy.DTOs.TransactionDTO;
import com.buddyapp.paymybuddy.auth.service.TokenService;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
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

    public LoginController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/token")
    public ResponseEntity<String> token(Authentication authentication) {
        log.debug("Token requested for user : " + authentication.getName());
        String token = tokenService.generateToken(authentication);
        log.debug("Token granted : " + token);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authentication", token);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("Response with header using ResponseEntity");
    }



    @GetMapping("/*")
    public String home(){
        return "Home !";
    }

    @GetMapping("/home")
    public String homeSweetHome(){
        return "Home sweet home";
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN', 'SCOPE_OAUTH2_USER')")
    @RequestMapping("/user")
    public String getUser(@RequestHeader("Authorization") String requestTokenHeader) {
        log.info("User");
        return "Welcome " + tokenService.decodeTokenUsername(requestTokenHeader);
    }

    @RequestMapping("/authi")
    public String getAuthi() {
        log.info("Authi");
        return "Welcome AUthi";
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @RequestMapping("/admin")
    public String getAdmin() {
        log.info("ADMIN");
        return "Welcome Admin";
    }

    @RequestMapping("/github/*")
    public String getGithub()
    {
        return "Welcome Github user!";
    }
}
