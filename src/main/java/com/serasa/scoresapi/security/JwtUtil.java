package com.serasa.scoresapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String segredo;
    
    @Value("${jwt.expiration}")
    private long tempoExpiracao;
    
    @Value("${jwt.prefix}")
    private String prefixo;

    private Key chaveAssinatura;

    @jakarta.annotation.PostConstruct
    public void init() {
        chaveAssinatura = Keys.hmacShaKeyFor(segredo.getBytes());
    }

    public String gerarToken(Authentication autenticacao) {
        String nomeUsuario = autenticacao.getName();

        List<String> permissoes = autenticacao.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(nomeUsuario)
                .claim("roles", permissoes)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tempoExpiracao))
                .signWith(chaveAssinatura, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(chaveAssinatura)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims extrairClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(chaveAssinatura)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public List<GrantedAuthority> getAuthorities(Claims claims) {
        var roles = (List<?>) claims.get("roles");
        return roles.stream()
                .map(papel -> (GrantedAuthority) () -> papel.toString())
                .collect(Collectors.toList());
    }

    public String recuperarToken(HttpServletRequest requisicao) {
        String cabecalho = requisicao.getHeader("Authorization");
        if (cabecalho != null && cabecalho.startsWith(prefixo + " ")) {
            return cabecalho.replace(prefixo + " ", "");
        }
        return null;
    }
}
