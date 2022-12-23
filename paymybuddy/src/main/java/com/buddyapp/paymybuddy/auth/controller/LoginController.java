package com.buddyapp.paymybuddy.auth.controller;


import jakarta.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping
    public String home(){
        return "Home !";
    }

    @RequestMapping("/user")
    @RolesAllowed("USER")
    public String getUser() {
        return "Welcome User";
    }

    @RequestMapping("/admin")
    @RolesAllowed("ADMIN")
    public String getAdmin() {
        return "Welcome Admin";
    }

    @RequestMapping("/github")
    public String getGithub()
    {
        return "Welcome Github user!";
    }
}
