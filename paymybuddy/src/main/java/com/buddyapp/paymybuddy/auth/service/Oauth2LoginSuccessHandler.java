package com.buddyapp.paymybuddy.auth.service;

import com.buddyapp.paymybuddy.DTOs.UserDTO;
import com.buddyapp.paymybuddy.constants.Provider;
import com.buddyapp.paymybuddy.exception.ExceptionHandler;
import com.buddyapp.paymybuddy.helper.CookieUtils;
import com.buddyapp.paymybuddy.models.Contact;
import com.buddyapp.paymybuddy.models.CustomOAuth2User;
import com.buddyapp.paymybuddy.models.Transaction;
import com.buddyapp.paymybuddy.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
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

        String userName = principal.getName();

        OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) principal);

        CustomOAuth2User user = new CustomOAuth2User( (OAuth2User) authToken.getPrincipal());

        Map<String, Object> attributes = user.getAttributes();

        String provider = authToken.getAuthorizedClientRegistrationId();

        String token = tokenService.generateToken(authentication);


        try{
            userService.getUserByUserName(userName);
            if (response.isCommitted()) {
                logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
                return;
            }
            response.setHeader("Token", token);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
            return;

        } catch (Exception e){
            //aint got shit
        }

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }


        switch (provider) {
            case "github":
                userService.createOauth2User(new UserDTO("NoMail goten from #" + userName, userName, attributes.get("login").toString(),"", null,
                        null, null, "ROLE_USER", 0., new ArrayList<Contact>(), new ArrayList<Transaction>()), Provider.GITHUB);
                break;
            case "google":
                userService.createOauth2User(new UserDTO(attributes.get("email").toString(), userName, attributes.get("name").toString(), "", attributes.get("given_name").toString(),
                        attributes.get("family_name").toString(), null, "ROLE_USER", 0., new ArrayList<Contact>(), new ArrayList<Transaction>()), Provider.GOOGLE);
                break;
            default:
                throw new ExceptionHandler("Identity provider not recognized");
        }

        response.setHeader("Token", token);
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

}
