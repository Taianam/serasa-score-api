package com.serasa.scoresapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.serasa.scoresapi.config.ViaCepTestConfiguration;
import com.serasa.scoresapi.domain.client.EnderecoResponse;
import com.serasa.scoresapi.domain.client.ViaCepClient;

import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Import(ViaCepTestConfiguration.class)
class SerasaScoreApiApplicationTests {

	@MockBean
	private ViaCepClient viaCepClient;

	@Test
	void contextLoads() {
		// Configurar o mock para retornar dados válidos
		EnderecoResponse enderecoResponse = new EnderecoResponse();
		enderecoResponse.setCep("01310100");
		enderecoResponse.setLogradouro("Avenida Paulista");
		enderecoResponse.setBairro("Bela Vista");
		enderecoResponse.setLocalidade("São Paulo");
		enderecoResponse.setUf("SP");
		
		when(viaCepClient.buscarCep(org.mockito.ArgumentMatchers.anyString()))
			.thenReturn(enderecoResponse);
		
		// Teste simples para verificar se o contexto Spring carrega corretamente
	}

}
