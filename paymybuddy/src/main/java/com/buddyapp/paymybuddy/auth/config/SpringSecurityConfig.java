package com.buddyapp.paymybuddy.auth.config;


import com.buddyapp.paymybuddy.auth.service.JpaUserDetailsService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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

    private final JpaUserDetailsService myUserDetailsService;

    public SpringSecurityConfig(PasswordEncoder encoder, JpaUserDetailsService myUserDetailsService) {
        this.encoder = encoder;
        this.myUserDetailsService = myUserDetailsService;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/home").permitAll()
                                .requestMatchers("/admin").hasRole("ADMIN")
                                .requestMatchers("/user").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()

                        )
                .userDetailsService(myUserDetailsService)
                .formLogin(withDefaults())
                .oauth2Login(withDefaults())
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    InMemoryUserDetailsManager usersManager(){
        return new InMemoryUserDetailsManager(
                User.withUsername("jean")
                        .password(encoder.encode("password"))
                        .roles("ADMIN")
                        .build()
        );
    }

}