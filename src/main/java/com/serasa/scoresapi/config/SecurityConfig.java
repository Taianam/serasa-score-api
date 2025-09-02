package com.serasa.scoresapi.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.serasa.scoresapi.security.CustomPasswordEncoder;
import com.serasa.scoresapi.security.CustomUserDetailsService;
import com.serasa.scoresapi.security.JwtFiltroAutorizacao;
import com.serasa.scoresapi.security.JwtUtil;
import com.serasa.scoresapi.security.PasswordUtil;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final JwtUtil jwtUtil;

	public SecurityConfig(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Bean
	public JwtFiltroAutorizacao jwtFiltroAutorizacao() {
		return new JwtFiltroAutorizacao(jwtUtil);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html",
								"/swagger-resources/**", "/webjars/**")
						.permitAll()
						.requestMatchers("/api/login").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/pessoas/**").hasAnyRole("ADMIN", "USER")
						.requestMatchers("/api/pessoas/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/api/scores/**").hasAnyRole("ADMIN", "USER")
						.requestMatchers("/api/scores/**").hasRole("ADMIN")
						.requestMatchers("/api/users/**").hasRole("ADMIN")
						.anyRequest().authenticated())
				.userDetailsService(userDetailsService)
				.addFilterBefore(jwtFiltroAutorizacao(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@SuppressWarnings("deprecation")
	@Bean
	public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder);
		return authProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder(PasswordUtil passwordUtil) {
		return new CustomPasswordEncoder(passwordUtil);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		var config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:3000"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

		var source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
