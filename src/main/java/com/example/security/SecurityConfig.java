package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity//activeaza spring securiti
public class SecurityConfig {

	
	//Face ca parolele sa se compare cu metoda BCrypt, nu se apeleaza manual aceasta metoda
	@Bean
	public PasswordEncoder passwordEncoder()//PasswordEncoder e o interfata pe care o foloseste spring security
	{
		 return new BCryptPasswordEncoder();
	}
}
