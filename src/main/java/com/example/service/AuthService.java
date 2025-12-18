package com.example.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.entity.Rol;
import com.example.entity.User;
import com.example.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerClient(User user) {

        if (user == null) throw new IllegalArgumentException("Date invalide!");

        // username
        if (user.getUsername() == null || user.getUsername().isBlank())
            throw new IllegalArgumentException("Username invalid!");

        // email
        if (user.getEmail() == null || user.getEmail().isBlank())
            throw new IllegalArgumentException("Email invalid!");
        if (userRepository.existsByEmail(user.getEmail()))
            throw new IllegalArgumentException("Există deja cont cu acest email!");

        // cnp
        if (user.getCnp() == null || user.getCnp().length() != 13)
            throw new IllegalArgumentException("CNP invalid (trebuie 13 cifre)!");
        if (userRepository.existsByCnp(user.getCnp()))
            throw new IllegalArgumentException("Există deja cont cu acest CNP!");

        // telefon
        if (user.getNrTelefon() == null || user.getNrTelefon().isBlank())
            throw new IllegalArgumentException("Telefon invalid!");

        // parola
        if (user.getPassword() == null || user.getPassword().isBlank())
            throw new IllegalArgumentException("Parola nu poate fi goală!");
        if (!passwordValidation(user.getPassword()))
            throw new IllegalArgumentException("Parola trebuie: min 8, 1 mică, 1 mare, 1 cifră, 1 simbol!");

        // IMPORTANT: clientul NU poate alege rol
        user.setRol(Rol.CLIENT);

        // IMPORTANT: parola se salvează hash-uită
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    private boolean passwordValidation(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");
    }
}
