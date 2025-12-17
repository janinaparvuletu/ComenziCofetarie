package com.example.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.entity.Rol;
import com.example.entity.User;
import com.example.repository.UserRepository;
import java.util.List;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	
	//constructor (injectare dependințe)
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder=passwordEncoder;
    }
    
    public List<User> findEmployees(){
    	return userRepository.findByRol(Rol.ANGAJAT);

    }
    
    
    public List<User> findUser(String username)
    {
    	return userRepository.findByUsername(username);
    }
    
    
    
    public User addUser(User user)
    {
        // CNP
        if (user.getCnp() == null || user.getCnp().length() != 13)
            throw new IllegalArgumentException("cnp-ul introdus este gresit!");
        if (userRepository.existsByCnp(user.getCnp()))
            throw new IllegalArgumentException("user-ul asta deja exista!");

        // EMAIL
        if (user.getEmail() == null || user.getEmail().isBlank())
            throw new IllegalArgumentException("email-ul introdus este gresit!");
        if (userRepository.existsByEmail(user.getEmail()))
            throw new IllegalArgumentException("exista deja un cont cu acest email!");
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));//
        return userRepository.save(user);
    }

    
    
    
    public void deleteUser(User user)
    {
    	if(!userRepository.existsByCnp(user.getCnp()))
    		throw new IllegalArgumentException("nu exista acest utilizator!");
    	
    	userRepository.deleteByCnp(user.getCnp());
    }
    
    
    
    public User changePassword(String cnp, String password)
    {
    	User user = userRepository.findByCnp(cnp)
    			.orElseThrow(()->new IllegalArgumentException("nu exista acest utilizator!"));
    	
    	if(!passwordValidation(password))
    		throw new IllegalArgumentException("parola nu e suficient de complexa!");
    	
    	user.setPassword(password);
    	return userRepository.save(user);
    }
    
    
    
    private boolean passwordValidation(String password) {
    	return password != null &&
    	           password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");
    }
    
    	
}
