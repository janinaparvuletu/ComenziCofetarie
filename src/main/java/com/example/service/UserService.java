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

    
    
    
    public void deleteUserByCnp(String cnp) {
        if (cnp == null || cnp.length() != 13)
            throw new IllegalArgumentException("cnp invalid!");
        if (!userRepository.existsByCnp(cnp))
            throw new IllegalArgumentException("nu exista acest utilizator!");
        userRepository.deleteByCnp(cnp);
    }

    
    
    
    public User changePasswordByEmail(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Nu există user cu acest email!"));

        if (!passwordValidation(password))
            throw new IllegalArgumentException("Parola nu este suficient de complexă!");

        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    
    
    private boolean passwordValidation(String password) {
    	return password != null &&
    	           password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");
    }
    
    	
}
