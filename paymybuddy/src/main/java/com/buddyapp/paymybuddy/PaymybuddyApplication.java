package com.buddyapp.paymybuddy;

import com.buddyapp.paymybuddy.DTOs.UserDTO;
import com.buddyapp.paymybuddy.auth.config.RsaKeyProperties;
import com.buddyapp.paymybuddy.constants.Provider;
import com.buddyapp.paymybuddy.contact.repository.ContactRepository;
import com.buddyapp.paymybuddy.entities.ContactEntity;
import com.buddyapp.paymybuddy.entities.TransactionEntity;
import com.buddyapp.paymybuddy.entities.UserEntity;
import com.buddyapp.paymybuddy.models.Contact;
import com.buddyapp.paymybuddy.models.Transaction;
import com.buddyapp.paymybuddy.user.repository.UserRepository;
import com.buddyapp.paymybuddy.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
@Slf4j
public class PaymybuddyApplication {

	public static void main(String[] args) {
		log.info("APPLICATION STARTED");
		SpringApplication.run(PaymybuddyApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserService users, PasswordEncoder encoder) {
		return args -> {
			users.createUser(new UserDTO("mailadmin.com", "usernameAdmin", "usernameAdmin", "password","firstName" ,
					"lastName", "0606060606", "ROLE_ADMIN", 1000., new ArrayList<Contact>(), new ArrayList<Transaction>()));

			users.createUser(new UserDTO("mailuser.com", "usernameUser", "usernameUser", "password","firstNameU" ,
					"lastNameU", "0606060606", "ROLE_USER", 1000., new ArrayList<Contact>(), new ArrayList<Transaction>()));
		};
	}

}
