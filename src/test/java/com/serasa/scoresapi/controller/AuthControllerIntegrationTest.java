package com.serasa.scoresapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serasa.scoresapi.domain.Role;
import com.serasa.scoresapi.domain.User;
import com.serasa.scoresapi.repository.UserRepository;
import com.serasa.scoresapi.config.ViaCepTestConfiguration;
import com.serasa.scoresapi.domain.client.EnderecoResponse;
import com.serasa.scoresapi.domain.client.ViaCepClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional(rollbackFor = Exception.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Import(ViaCepTestConfiguration.class)
@TestPropertySource(properties = {
    "spring.sql.init.mode=never",
    "spring.jpa.defer-datasource-initialization=false"
})
class AuthControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ViaCepClient viaCepClient;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		// Limpar dados existentes antes de cada teste
		userRepository.deleteAll();
		
		// Configurar mock do ViaCep
		configurarMockViaCep();
		
		User admin = new User();
		admin.setUsername("admin");
		admin.setPassword(passwordEncoder.encode("123456"));
		admin.setRole(Role.ADMIN);
		admin.setAtivo(true);
		userRepository.save(admin);

		User user = new User();
		user.setUsername("user");
		user.setPassword(passwordEncoder.encode("123456"));
		user.setRole(Role.USER);
		user.setAtivo(true);
		userRepository.save(user);
	}

	private void configurarMockViaCep() {
		EnderecoResponse enderecoResponse = new EnderecoResponse();
		enderecoResponse.setCep("01310100");
		enderecoResponse.setLogradouro("Avenida Paulista");
		enderecoResponse.setBairro("Bela Vista");
		enderecoResponse.setLocalidade("São Paulo");
		enderecoResponse.setUf("SP");
		
		when(viaCepClient.buscarCep(org.mockito.ArgumentMatchers.anyString()))
			.thenReturn(enderecoResponse);
	}

	@Test
	void deveRealizarLoginComSucessoParaAdmin() throws Exception {
		Map<String, String> loginRequest = Map.of("username", "admin", "password", "123456");

		mockMvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.token").exists())
				.andExpect(jsonPath("$.token").isString()).andExpect(jsonPath("$.token").isNotEmpty());
	}

	@Test
	void deveRealizarLoginComSucessoParaUser() throws Exception {
		Map<String, String> loginRequest = Map.of("username", "user", "password", "123456");

		mockMvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists());
	}

	@Test
	void deveRetornarErroParaCredenciaisInvalidas() throws Exception {
		Map<String, String> loginRequest = Map.of("username", "usuario_inexistente", "password", "senha_errada");

		mockMvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isUnauthorized());
	}

	@Test
	void deveRetornarErroParaSenhaIncorreta() throws Exception {
		Map<String, String> loginRequest = Map.of("username", "admin", "password", "senha_errada");

		mockMvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isUnauthorized());
	}

	@Test
	void deveRetornarErroParaUsernameVazio() throws Exception {
		Map<String, String> loginRequest = Map.of("username", "", "password", "123456");

		mockMvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void deveRetornarErroParaPasswordVazio() throws Exception {
		Map<String, String> loginRequest = Map.of("username", "admin", "password", "");

		mockMvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void deveRetornarErroParaRequestVazio() throws Exception {
		mockMvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isBadRequest());
	}

}
