package com.buddyapp.paymybuddy.auth.controller;


import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginController {

    @GetMapping("/*")
    public String home(){
        return "Home !";
    }

    @GetMapping("/home")
    public String homeSweetHome(){
        return "Home sweet home";
    }

    //@PreAuthorize("hasRole('USER')")
    @RequestMapping("/user")
    public String getUser() {
        log.info("User");
        return "Welcome User";
    }

    //@PreAuthorize("hasRole('ADMIN')")
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
