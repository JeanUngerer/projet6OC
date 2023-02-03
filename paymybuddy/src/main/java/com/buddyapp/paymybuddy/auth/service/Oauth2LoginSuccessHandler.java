package com.buddyapp.paymybuddy.auth.service;

import com.buddyapp.paymybuddy.DTOs.UserDTO;
import com.buddyapp.paymybuddy.constants.Provider;
import com.buddyapp.paymybuddy.exception.ExceptionHandler;
import com.buddyapp.paymybuddy.helper.CookieUtils;
import com.buddyapp.paymybuddy.models.Contact;
import com.buddyapp.paymybuddy.models.Transaction;
import com.buddyapp.paymybuddy.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

import static com.buddyapp.paymybuddy.constants.CookiesName.REDIRECT_URI_PARAM_COOKIE_NAME;

@Service
public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        Principal principal = (Principal) authentication;

        String userName = "oauth2user";
        userName = principal.getName();

        String token = tokenService.generateToken(authentication);


        try{
            userService.getUserByUserName(userName);
            if (response.isCommitted()) {
                logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
                return;
            }
            response.addHeader("Token", token);
            clearAuthenticationAttributes(request, response);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
            return;

        } catch (Exception e){
            //aint got shit
        }

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        userService.createOauth2User(new UserDTO("NoMail goten from " + userName, userName,"No auth via password for " + userName, null,
                null, null, authentication.getAuthorities().toString(), 0., new ArrayList<Contact>(), new ArrayList<Transaction>()), Provider.GITHUB);

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }


    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);

        if (redirectUri.isPresent()) {
            throw new ExceptionHandler("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        return UriComponentsBuilder.fromUriString(targetUrl).build().toUriString();
    }

    public void OLDonAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String token = tokenService.generateToken(authentication);
        Principal principal = (Principal) authentication;

        String userName = "oauth2user";
        userName = principal.getName();
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



    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
    }
    private void setResponse(HttpServletResponse response, String token) {
        response.setHeader("Token", token);
    }

}
