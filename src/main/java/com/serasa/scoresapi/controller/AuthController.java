package com.serasa.scoresapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.serasa.scoresapi.dto.request.LoginRequest;
import com.serasa.scoresapi.dto.response.LoginResponse;
import com.serasa.scoresapi.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Tag(name = "Autenticação", description = "Operações de autenticação")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
 
    }

    @PostMapping("/login")
    @Operation(
        summary = "Login", 
        description = "Realiza autenticação e retorna token JWT. " +
                     "Envie JSON com 'username' e 'password'. " +
                     "Usuários de teste: admin/123456 (ADMIN), user/123456 (USER)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso - retorna token JWT"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
        public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            String token = jwtUtil.gerarToken(authentication);
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("{\"error\": \"Credenciais inválidas\"}");
        }
    }
}
