package com.serasa.scoresapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serasa.scoresapi.dto.request.PessoaRequest;
import com.serasa.scoresapi.config.ViaCepTestConfiguration;
import com.serasa.scoresapi.domain.client.EnderecoResponse;
import com.serasa.scoresapi.domain.client.ViaCepClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Import(ViaCepTestConfiguration.class)
class PessoaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ViaCepClient viaCepClient;

    private PessoaRequest pessoaRequest;

    @BeforeEach
    void setUp() {
        pessoaRequest = criarPessoaRequestPadrao();
        configurarMockViaCep();
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

    private PessoaRequest criarPessoaRequestPadrao() {
        PessoaRequest request = new PessoaRequest();
        request.setNome("João Silva");
        request.setIdade(30);
        request.setCep("01310100");
        request.setTelefone("11999999999");
        request.setScore(750);
        return request;
    }

    private PessoaRequest criarPessoaRequestAtualizado(PessoaRequest original) {
        PessoaRequest updateRequest = new PessoaRequest();
        updateRequest.setNome("João Silva Atualizado");
        updateRequest.setIdade(32);
        updateRequest.setCep("01310100");
        updateRequest.setTelefone("11988888888");
        updateRequest.setScore(850);
        return updateRequest;
    }


    private Long criarPessoaNoBanco(PessoaRequest request) throws Exception {
        String response = mockMvc.perform(post("/api/pessoas")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveCriarPessoaComSucesso() throws Exception {
        mockMvc.perform(post("/api/pessoas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoaRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(pessoaRequest.getNome()))
                .andExpect(jsonPath("$.idade").value(pessoaRequest.getIdade()))
                .andExpect(jsonPath("$.cep").value(pessoaRequest.getCep()))
                .andExpect(jsonPath("$.score").value(pessoaRequest.getScore()));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deveListarPessoasComSucesso() throws Exception {
        mockMvc.perform(get("/api/pessoas"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deveListarPessoasComFiltrosComSucesso() throws Exception {
        mockMvc.perform(get("/api/pessoas/paginado")
                .param("nome", "João")
                .param("idade", "30")
                .param("page", "0")
                .param("size", "10"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deveBuscarPessoaPorIdComSucesso() throws Exception {
        PessoaRequest request = criarPessoaRequestPadrao();
        Long pessoaId = criarPessoaNoBanco(request);

        mockMvc.perform(get("/api/pessoas/" + pessoaId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nome").value(request.getNome()))
               .andExpect(jsonPath("$.idade").value(request.getIdade()))
               .andExpect(jsonPath("$.cep").value(request.getCep()))
               .andExpect(jsonPath("$.score").value(request.getScore()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveAtualizarPessoaComSucesso() throws Exception {
        PessoaRequest createRequest = criarPessoaRequestPadrao();
        Long pessoaId = criarPessoaNoBanco(createRequest);

        PessoaRequest updateRequest = criarPessoaRequestAtualizado(createRequest);

        mockMvc.perform(put("/api/pessoas/" + pessoaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(updateRequest.getNome()))
                .andExpect(jsonPath("$.idade").value(updateRequest.getIdade()))
                .andExpect(jsonPath("$.score").value(updateRequest.getScore()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveDeletarPessoaComSucesso() throws Exception {
        PessoaRequest deleteRequest = criarPessoaRequestPadrao();
        Long pessoaId = criarPessoaNoBanco(deleteRequest);

        mockMvc.perform(delete("/api/pessoas/" + pessoaId))
               .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarForbiddenSemAutenticacao() throws Exception {
        mockMvc.perform(get("/api/pessoas"))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deveRetornarForbiddenParaUsuarioSemPermissao() throws Exception {
        mockMvc.perform(post("/api/pessoas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoaRequest)))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveValidarCamposObrigatorios() throws Exception {
        PessoaRequest requestInvalido = new PessoaRequest();


        mockMvc.perform(post("/api/pessoas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))
               .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveValidarScoreForaDoRange() throws Exception {
        PessoaRequest request = criarPessoaRequestPadrao();
        request.setScore(1500); // Score inválido

        mockMvc.perform(post("/api/pessoas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest());
    }
}
