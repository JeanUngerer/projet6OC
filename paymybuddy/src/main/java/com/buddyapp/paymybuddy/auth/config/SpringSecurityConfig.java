package com.buddyapp.paymybuddy.auth.config;


import com.buddyapp.paymybuddy.auth.service.CustomOAuth2UserService;
import com.buddyapp.paymybuddy.auth.service.Oauth2LoginHandler;
import com.buddyapp.paymybuddy.models.CustomOAuth2User;
import com.buddyapp.paymybuddy.user.service.UserService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;
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
    private Oauth2LoginHandler handler;

    private final UserService myUserDetailsService;

    public SpringSecurityConfig(PasswordEncoder encoder, RsaKeyProperties rsaKeys, UserService myUserDetailsService) {
        this.encoder = encoder;
        this.myUserDetailsService = myUserDetailsService;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/register").permitAll()
                        //.requestMatchers("/token").permitAll()
                        .anyRequest().authenticated()

                        )
                //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .userDetailsService(myUserDetailsService)
                .formLogin(withDefaults())
                .oauth2Login(oauth -> oauth
                        //.tokenEndpoint(authentication -> handler.generateToken())
                        //.accessTokenResponseClient()
                        //.userInfoEndpoint()
                       // .userService(customOAuth2UserService)
                      //  .and()
                        .successHandler((request, response, authentication) -> {

                            handler.onAuthenticationSuccess(request, response, authentication);

                            //response.setHeader("Token", "token");
                        })
                        .failureHandler((request, response, exception) -> {
                            request.getSession().setAttribute("error.message", exception.getMessage());
                            //handler.onAuthenticationFailure(request, response, exception);
                        }))
               // .oauth2Login(withDefaults())
                .httpBasic(withDefaults())
                .build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.addExposedHeader("Token");
        configuration.addAllowedMethod(HttpMethod.PUT);
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }


}