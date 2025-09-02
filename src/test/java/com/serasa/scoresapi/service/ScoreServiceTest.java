package com.serasa.scoresapi.service;

import com.serasa.scoresapi.domain.Score;
import com.serasa.scoresapi.domain.Pessoa;
import com.serasa.scoresapi.repository.ScoreRepository;
import com.serasa.scoresapi.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScoreServiceTest {

    @Mock
    private ScoreRepository scoreRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private ScoreClassificacaoService scoreClassificacaoService;

    @InjectMocks
    private com.serasa.scoresapi.service.impl.ScoreServiceImpl scoreService;

    private Pessoa pessoa;
    private Score score;

    @BeforeEach
    void setUp() {
        pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("João Silva");
        pessoa.setIdade(30);
        pessoa.setScore(650);

        score = new Score();
        score.setId(1L);
        score.setPessoa(pessoa);
        score.setScore(750);
        score.setDataRegistro(LocalDateTime.now());
        score.setDescricao("Recomendável");
    }

    @Test
    void deveRegistrarScoreComSucesso() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(scoreClassificacaoService.obterDescricaoScore(750)).thenReturn("Recomendável");
        when(scoreRepository.save(any(Score.class))).thenReturn(score);
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        scoreService.registrarScore(1L, 750);

        verify(scoreRepository).save(any(Score.class));
        verify(pessoaRepository).save(any(Pessoa.class));
        verify(scoreClassificacaoService).obterDescricaoScore(750);
    }

    @Test
    void deveRetornarErroAoRegistrarScoreParaPessoaInexistente() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> scoreService.registrarScore(1L, 750));
        verify(scoreRepository, never()).save(any(Score.class));
    }


}
