package com.croche.lariharumi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.croche.lariharumi.infra.security.TokenService;
import com.croche.lariharumi.models.User.User;
import com.croche.lariharumi.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder PasswordEncoder;

    public String register(String name, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(PasswordEncoder.encode(password));
        user.setRole(User.Role.USER); // Default role

        userRepository.save(user);
        return tokenService.generateToken(user);  // JWT token generation
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (PasswordEncoder.matches(password, user.getPassword())) {
            return tokenService.generateToken(user);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}
