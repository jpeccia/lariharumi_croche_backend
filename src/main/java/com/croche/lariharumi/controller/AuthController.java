package com.croche.lariharumi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.croche.lariharumi.dto.LoginData;
import com.croche.lariharumi.dto.RegisterData;
import com.croche.lariharumi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;



@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Endpoint para registro de um novo usuário.
     * @param registerData Dados para o registro de um novo usuário
     * @return Token gerado após o registro do usuário
     */
    @PostMapping("/register")
    @Operation(
        summary = "Registrar um novo usuário",
        description = "Registra um novo usuário e retorna um token JWT para autenticação."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201", 
            description = "Usuário registrado com sucesso e token gerado", 
            content = @Content(mediaType = "application/json", schema = @Schema(type = "string"))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Dados inválidos fornecidos",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<String> register(@Valid @RequestBody RegisterData registerData) {
        String token = authService.register(registerData.name(), registerData.email(), registerData.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    /**
     * Endpoint para login de um usuário existente.
     * @param loginData Dados de login (email e senha)
     * @return Token gerado após o login
     */
    @PostMapping("/login")
    @Operation(
        summary = "Login de usuário",
        description = "Autentica o usuário com email e senha e retorna um token JWT."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Login bem-sucedido e token gerado", 
            content = @Content(mediaType = "application/json", schema = @Schema(type = "string"))
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Credenciais inválidas",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Dados inválidos fornecidos",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<String> login(@Valid @RequestBody LoginData loginData) {
        String token = authService.login(loginData.email(), loginData.password());
        return ResponseEntity.ok(token);
    }
}
