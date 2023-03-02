package com.buddyapp.paymybuddy.auth.controller;


import com.buddyapp.paymybuddy.DTOs.MessageDTO;
import com.buddyapp.paymybuddy.DTOs.RegistrationDTO;
import com.buddyapp.paymybuddy.auth.service.TokenService;
import com.buddyapp.paymybuddy.models.CustomOAuth2User;
import com.buddyapp.paymybuddy.user.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.security.Principal;
import java.util.Map;

@RestController
@Slf4j
public class LoginController {

    private final TokenService tokenService;

    private final UserService userService;

    private final OAuth2AuthorizedClientService authorizedClientService;

    public LoginController(TokenService tokenService, UserService userService, OAuth2AuthorizedClientService authorizedClientService) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.authorizedClientService = authorizedClientService;
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








    @RequestMapping("/**")
    public ResponseEntity<MessageDTO> getUserInfo(Principal user) {
        StringBuffer userInfo= new StringBuffer();

        if(user instanceof UsernamePasswordAuthenticationToken){
            userInfo.append(getUsernamePasswordLoginInfo(user));
        }
        else if(user instanceof OAuth2AuthenticationToken){
            userInfo.append(getOauth2LoginInfo(user));
        }

        String token =tokenService.generateToken((Authentication) user);


        if(!token.isEmpty()){
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Token", token);
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .build();
        }



        return ResponseEntity.ok(new MessageDTO(userInfo.toString()));

    }


    //@RequestMapping("/**")
    public String other(Principal user){
        return user.toString();
    }




    @GetMapping("/home")
    public ResponseEntity<MessageDTO> homeSweetHome(){
        return ResponseEntity.ok(new MessageDTO("Hi home !"));
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
    public ResponseEntity<MessageDTO> getAuthi(@RequestHeader("Authorization") String requestTokenHeader) {
        log.info("Authi");
        if(requestTokenHeader == null || requestTokenHeader.equals("")){return ResponseEntity.ok(new MessageDTO("Not AUthenticated !"));}

        return ResponseEntity.ok(new MessageDTO("Hi " + userService.getUserByUserName(tokenService.decodeTokenUsername(requestTokenHeader)).getLogin()));

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





    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private StringBuffer getUsernamePasswordLoginInfo(Principal user)
    {
        StringBuffer usernameInfo = new StringBuffer();

        UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) user);
        if(token.isAuthenticated()){
            User u = (User) token.getPrincipal();
            usernameInfo.append("Welcome, " + u.getUsername());
        }
        else{
            usernameInfo.append("NA");
        }
        return usernameInfo;
    }

    private StringBuffer getOauth2LoginInfo(Principal user){
        StringBuffer protectedInfo = new StringBuffer();
        OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) user);
        OAuth2AuthorizedClient authClient = this.authorizedClientService.loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(), authToken.getName());
        OAuth2User principal = ((OAuth2AuthenticationToken) user).getPrincipal();
        OidcIdToken idToken = getIdToken(principal);
        log.info("idToken: " + idToken);

        if(authToken.isAuthenticated()){

            CustomOAuth2User user0Auth2 = new CustomOAuth2User( (OAuth2User) authToken.getPrincipal());

            Map<String, Object> userAttributes = user0Auth2.getAttributes();

            //Map<String,Object> userAttributes = ((DefaultOAuth2User) authToken.getPrincipal()).getAttributes();

            String userToken = authClient.getAccessToken().getTokenValue();
            protectedInfo.append("Welcome, " + userAttributes.get("name")+"<br><br>");
            protectedInfo.append("e-mail: " + userAttributes.get("email")+"<br><br>");
            //protectedInfo.append("Access Token: " + userToken+"<br><br>");

            if(idToken != null) {

                protectedInfo.append("idToken value: " + idToken.getTokenValue()+"<br><br>");
                protectedInfo.append("Token mapped values <br><br>");

                Map<String, Object> claims = idToken.getClaims();

                for (String key : claims.keySet()) {
                    protectedInfo.append("  " + key + ": " + claims.get(key)+"<br>");
                }
            }
        }
        else{
            protectedInfo.append("NA");
        }
        return protectedInfo;
    }

    private OidcIdToken getIdToken(OAuth2User principal){
        if(principal instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser)principal;
            return oidcUser.getIdToken();
        }
        return null;
    }
}
