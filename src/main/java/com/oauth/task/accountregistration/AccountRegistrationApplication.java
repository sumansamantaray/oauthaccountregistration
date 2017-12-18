package com.oauth.task.accountregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class AccountRegistrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountRegistrationApplication.class, args);
	}
}
