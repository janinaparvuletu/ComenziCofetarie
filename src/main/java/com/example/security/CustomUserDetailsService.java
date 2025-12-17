package com.example.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService  implements UserDetailsService{

	private final UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) {
	    this.userRepository = userRepository;
	}	
	
	//metoda care face autentificarea
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
			    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

		//GrantedAuthority e o interfata din Spring Security, ce reprezinta un drept, o autorizatie.
		GrantedAuthority authority =
			    new SimpleGrantedAuthority("ROLE_" + user.getRol().name());//name e metoda din enum ce transforma
																			// enum urile in string
											//cuvantul ROLE_ e obligatoriu, security nu recunoaste rolul.
				//cu Simple Granted Authority se preia rolul user ului in curs, care incearca sa se conecteze
		
		Collection<? extends GrantedAuthority> authorities = List.of(authority);
		//se creeaza o colectie imutabila in care se salveaza un singur rol, intrucat un user poatte avea mai multe.

		
		return new org.springframework.security.core.userdetails.User(
			    user.getEmail(),
			    user.getPassword(),
			    authorities
			);

	}

}
