package com.buddyapp.paymybuddy;

import com.buddyapp.paymybuddy.auth.config.RsaKeyProperties;
import com.buddyapp.paymybuddy.constants.Provider;
import com.buddyapp.paymybuddy.entities.ContactEntity;
import com.buddyapp.paymybuddy.entities.TransactionEntity;
import com.buddyapp.paymybuddy.entities.UserEntity;
import com.buddyapp.paymybuddy.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

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
	CommandLineRunner commandLineRunner(UserRepository users, PasswordEncoder encoder) {
		return args -> {
			users.save(new UserEntity(1L, Provider.LOCAL, "mailadmin.com",encoder.encode("password"),"firstName" ,
					"lastName", "0606060606", "ROLE_ADMIN", 1000.,
					new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));

			users.save(new UserEntity(2L, Provider.LOCAL, "mailuser.com",encoder.encode("password"),"firstNameU" ,
					"lastNameU", "0606060606", "ROLE_USER", 1000.,
					new ArrayList<ContactEntity>(), new ArrayList<TransactionEntity>()));

		};
	}

}
