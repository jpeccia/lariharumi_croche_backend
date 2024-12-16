package com.croche.lariharumi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.croche.lariharumi.dto.RegisterData;
import com.croche.lariharumi.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Endpoint para registro de usuário
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterData registerData) {
        // Extrair os dados de registerData
        String name = registerData.name();
        String email = registerData.email();
        String password = registerData.password();
    
        // Chamar o serviço de autenticação para registrar o usuário e gerar o token
        String token = authService.register(name, email, password);
        
        // Retorna o token gerado
        return ResponseEntity.ok(token);
    }
    

    // Endpoint para login de usuário
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        String token = authService.login(email, password);
        return ResponseEntity.ok(token);  // Retorna o token gerado
    }
}
