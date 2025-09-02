package com.serasa.scoresapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;

import java.io.IOException;

public class JwtFiltroAutorizacao extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFiltroAutorizacao(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest requisicao,
                                    HttpServletResponse resposta,
                                    FilterChain cadeiaDeFiltros)
            throws ServletException, IOException {
        

        if ("/api/login".equals(requisicao.getRequestURI()) && "POST".equals(requisicao.getMethod())) {
            cadeiaDeFiltros.doFilter(requisicao, resposta);
            return;
        }

        String token = jwtUtil.recuperarToken(requisicao);

        if (token != null && jwtUtil.validarToken(token)) {
            Claims dadosToken = jwtUtil.extrairClaims(token);
            String nomeUsuario = dadosToken.getSubject();
            var permissoes = jwtUtil.getAuthorities(dadosToken);

            var autenticacao = new UsernamePasswordAuthenticationToken(
                    nomeUsuario, null, permissoes);
            autenticacao.setDetails(new WebAuthenticationDetailsSource().buildDetails(requisicao));
            SecurityContextHolder.getContext().setAuthentication(autenticacao);
        }

        cadeiaDeFiltros.doFilter(requisicao, resposta);
    }
}
