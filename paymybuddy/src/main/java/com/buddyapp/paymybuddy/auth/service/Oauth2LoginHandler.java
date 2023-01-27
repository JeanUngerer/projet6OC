package com.buddyapp.paymybuddy.auth.service;

import com.buddyapp.paymybuddy.DTOs.UserDTO;
import com.buddyapp.paymybuddy.constants.Provider;
import com.buddyapp.paymybuddy.models.Contact;
import com.buddyapp.paymybuddy.models.Transaction;
import com.buddyapp.paymybuddy.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class Oauth2LoginHandler {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;


    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String token = tokenService.generateToken(authentication);
        Principal principal = (Principal) authentication;

        String userName = "oauth2user";
        try{
            userService.getUserByUserName(userName);
            setResponse(response, token);
            return;

        } catch (Exception e){
            //aint got shit
        }
        userService.createOauth2User(new UserDTO("NoMail goten from " + userName, userName,"No auth via password for " + userName, null,
                null, null, authentication.getAuthorities().toString(), 0., new ArrayList<Contact>(), new ArrayList<Transaction>()), Provider.GITHUB);

        setResponse(response, token);
    }

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, Exception exception) {

    }

    private void setResponse(HttpServletResponse response, String token) {
        response.setHeader("Token", token);
    }

    public String generateToken() {
        return "token";
    }
}
