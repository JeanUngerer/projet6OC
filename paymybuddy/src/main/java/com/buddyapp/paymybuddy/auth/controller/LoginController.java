package com.buddyapp.paymybuddy.auth.controller;


import jakarta.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginController {

    @GetMapping
    public String home(){
        return "Home !";
    }

    @RequestMapping("/user")
    @RolesAllowed("USER")
    public String getUser() {
        log.info("User");
        return "Welcome User";
    }

    @RequestMapping("/admin")
    @RolesAllowed("ADMIN")
    public String getAdmin() {
        log.info("ADMIN");
        return "Welcome Admin";
    }

    @RequestMapping("/github")
    public String getGithub()
    {
        return "Welcome Github user!";
    }
}
