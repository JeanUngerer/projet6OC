package com.buddyapp.paymybuddy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class PaymybuddyApplication {

	public static void main(String[] args) {
		log.info("APPLICATION STARTED");
		SpringApplication.run(PaymybuddyApplication.class, args);
	}

}
