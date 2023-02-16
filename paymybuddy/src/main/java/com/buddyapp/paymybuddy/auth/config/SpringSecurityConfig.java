package com.buddyapp.paymybuddy.auth.config;


import com.buddyapp.paymybuddy.auth.service.Oauth2LoginFailureHandler;
import com.buddyapp.paymybuddy.user.service.CustomOAuth2UserService;
import com.buddyapp.paymybuddy.auth.service.OAuth2AccessTokenResponseConverterWithDefaults;
import com.buddyapp.paymybuddy.auth.service.Oauth2LoginSuccessHandler;
import com.buddyapp.paymybuddy.user.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import java.util.Collections;
import java.util.List;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
@Getter
@Setter
public class SpringSecurityConfig {

    private static List<String> clients = Arrays.asList("github");

    private final PasswordEncoder encoder;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;


    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private UserService userService;

    @Autowired
    private Oauth2LoginSuccessHandler successHandler;

    @Autowired
    private Oauth2LoginFailureHandler failureHandler;

    private final UserService myUserDetailsService;

    public SpringSecurityConfig(PasswordEncoder encoder, RsaKeyProperties rsaKeys, UserService myUserDetailsService) {
        this.encoder = encoder;
        this.myUserDetailsService = myUserDetailsService;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login**", "/oauth2/**", "/authi").permitAll()
                        .anyRequest().authenticated()
                        )
                //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .userDetailsService(myUserDetailsService)
                .formLogin(withDefaults())
                .oauth2Login(oauth -> oauth

                        //.authorizationEndpoint()
                        //.authorizationRequestRepository( new InMemoryRequestRepository() )
                        //.and()
                        .redirectionEndpoint()
                        .and()
                        .userInfoEndpoint()
                        //.userService(userService)
                        .and()
                        .tokenEndpoint()
                        .and()
                        .successHandler((request, response, authentication) -> {
                            successHandler.onAuthenticationSuccess(request, response, authentication);
                            //response.setHeader("Token", "token");
                        })
                        .failureHandler((request, response, exception) -> {
                            //request.getSession().setAttribute("error.message", exception.getMessage());
                            failureHandler.onAuthenticationFailure(request, response, exception);
                        }))
                //.oauth2Login(withDefaults())
                .httpBasic(withDefaults())
                .build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.addExposedHeader("Token");
        configuration.addAllowedMethod(HttpMethod.PUT);
        //configuration.setAllowedMethods( Collections.singletonList( "*" ) );
        //configuration.setAllowedOrigins( Collections.singletonList( "*" ) );
        //configuration.setAllowedHeaders( Collections.singletonList( "*" ) );
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
/*
    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> authorizationCodeTokenResponseClient() {
        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
        tokenResponseHttpMessageConverter.setTokenResponseConverter(new OAuth2AccessTokenResponseConverterWithDefaults());
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        DefaultAuthorizationCodeTokenResponseClient tokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
        tokenResponseClient.setRestOperations(restTemplate);
        return tokenResponseClient;
    }*/
}