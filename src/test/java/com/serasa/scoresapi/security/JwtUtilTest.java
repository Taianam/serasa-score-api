package com.serasa.scoresapi.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        ReflectionTestUtils.setField(jwtUtil, "segredo", "test-secret-key-for-testing-purposes-only-must-be-long-enough");
        ReflectionTestUtils.setField(jwtUtil, "tempoExpiracao", 86400000L);
        ReflectionTestUtils.setField(jwtUtil, "prefixo", "Bearer");
        jwtUtil.init();
    }

    @Test
    void deveGerarTokenComSucesso() {

        List<SimpleGrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority("ROLE_ADMIN")
        );
        Authentication auth = new UsernamePasswordAuthenticationToken("admin", "password", authorities);

        String token = jwtUtil.gerarToken(auth);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.startsWith("eyJ")); // JWT sempre começa com eyJ
    }

    @Test
    void deveValidarTokenValido() {

        List<SimpleGrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority("ROLE_USER")
        );
        Authentication auth = new UsernamePasswordAuthenticationToken("user", "password", authorities);
        String token = jwtUtil.gerarToken(auth);


        boolean isValid = jwtUtil.validarToken(token);

        assertTrue(isValid);
    }

    @Test
    void deveRejeitarTokenInvalido() {
        String tokenInvalido = "token.invalido.aqui";

        boolean isValid = jwtUtil.validarToken(tokenInvalido);

        assertFalse(isValid);
    }

    @Test
    void deveRejeitarTokenMalformado() {
        String tokenMalformado = "eyJ.malformed.token";

        boolean isValid = jwtUtil.validarToken(tokenMalformado);

        assertFalse(isValid);
    }
}
