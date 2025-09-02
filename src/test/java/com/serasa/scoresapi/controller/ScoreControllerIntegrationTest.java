package com.serasa.scoresapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.serasa.scoresapi.domain.Pessoa;
import com.serasa.scoresapi.domain.Score;
import com.serasa.scoresapi.repository.PessoaRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class ScoreControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PessoaRepository pessoaRepository;

    private Pessoa pessoa;
    private Score score;

    @BeforeEach
    void setUp() {
        // Limpar dados existentes antes de cada teste
        pessoaRepository.deleteAll();
        
        pessoa = new Pessoa();
        pessoa.setNome("João Silva");
        pessoa.setIdade(30);
        pessoa.setCep("01310100");
        pessoa.setTelefone("11999999999");
        pessoa.setScore(650);
        pessoa.setAtivo(true);
        pessoa.setEstado("SP");
        pessoa.setCidade("São Paulo");
        pessoa.setBairro("Bela Vista");
        pessoa.setLogradouro("Av. Paulista");

        pessoa = pessoaRepository.save(pessoa);

        score = new Score();
        score.setPessoa(pessoa);
        score.setScore(750);
        score.setDataRegistro(LocalDateTime.now());
        score.setDescricao("Recomendável");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRegistrarScoreComSucesso() throws Exception {
        mockMvc.perform(post("/api/scores/pessoas/{pessoaId}", pessoa.getId())
                        .param("score", "750"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deveRetornarForbiddenAoRegistrarScoreComUser() throws Exception {
        mockMvc.perform(post("/api/scores/pessoas/{pessoaId}", pessoa.getId())
                        .param("score", "750"))
                .andExpect(status().isForbidden());
    }


}
