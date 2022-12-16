package com.buddyapp.paymybuddy.auth.controller;


import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginController {

    @RequestMapping("/*")
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
}
