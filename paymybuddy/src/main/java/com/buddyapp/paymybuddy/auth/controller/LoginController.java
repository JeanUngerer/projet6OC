package com.buddyapp.paymybuddy.auth.controller;


import com.buddyapp.paymybuddy.auth.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginController {

    private final TokenService tokenService;

    public LoginController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/token")
    public  String token(Authentication authentication) {
        log.debug("Token requested for user : " + authentication.getName());
        String token = tokenService.generateToken(authentication);
        log.debug("Token granted : " + token);
        return token;
    }



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

    @RequestMapping("/authi")
    public String getAuthi() {
        log.info("Authi");
        return "Welcome AUthi";
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
