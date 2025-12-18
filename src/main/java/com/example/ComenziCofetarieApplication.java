package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.entity.Rol;
import com.example.entity.User;
import com.example.repository.UserRepository;

@SpringBootApplication
public class ComenziCofetarieApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComenziCofetarieApplication.class, args);
	}
	
	@Bean
	CommandLineRunner seed(UserRepository userRepo, PasswordEncoder encoder) {
	    return args -> {
	        if (userRepo.findByEmail("admin@test.ro").isEmpty()) {

	            User admin = new User();
	            admin.setUsername("admin");
	            admin.setEmail("admin@test.ro");
	            admin.setCnp("6050201250067");
	            admin.setNrTelefon("0711111111");
	            admin.setRol(Rol.MANAGER);

	            // obligatoriu: parola criptată
	            admin.setPassword(encoder.encode("Admin123!"));

	            userRepo.save(admin);
	        }
	    };
	}

}
