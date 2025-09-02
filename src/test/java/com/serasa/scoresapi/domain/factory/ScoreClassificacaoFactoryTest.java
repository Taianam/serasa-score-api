package com.serasa.scoresapi.domain.factory;

import com.serasa.scoresapi.domain.strategy.ScoreClassificacaoStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoreClassificacaoFactoryTest {

    @Mock
    private ScoreClassificacaoStrategy insuficienteStrategy;
    
    @Mock
    private ScoreClassificacaoStrategy inaceitavelStrategy;
    
    @Mock
    private ScoreClassificacaoStrategy aceitavelStrategy;
    
    @Mock
    private ScoreClassificacaoStrategy recomendavelStrategy;

    private ScoreClassificacaoFactory factory;

    @BeforeEach
    void setUp() {
        List<ScoreClassificacaoStrategy> strategies = Arrays.asList(
            insuficienteStrategy, inaceitavelStrategy, aceitavelStrategy, recomendavelStrategy
        );
        factory = new ScoreClassificacaoFactory(strategies);
    }

    @Test
    void deveCriarEstrategiaInsuficiente() {
        when(insuficienteStrategy.aplicavel(100)).thenReturn(true);
        when(insuficienteStrategy.getDescricao()).thenReturn("Insuficiente");

        ScoreClassificacaoStrategy strategy = factory.criarEstrategia(100);

        assertNotNull(strategy);
        assertEquals("Insuficiente", strategy.getDescricao());
    }

    @Test
    void deveCriarEstrategiaInaceitavel() {
        when(inaceitavelStrategy.aplicavel(300)).thenReturn(true);
        when(inaceitavelStrategy.getDescricao()).thenReturn("Inaceitável");

        ScoreClassificacaoStrategy strategy = factory.criarEstrategia(300);

        assertNotNull(strategy);
        assertEquals("Inaceitável", strategy.getDescricao());
    }

    @Test
    void deveCriarEstrategiaAceitavel() {
        when(aceitavelStrategy.aplicavel(600)).thenReturn(true);
        when(aceitavelStrategy.getDescricao()).thenReturn("Aceitável");

        ScoreClassificacaoStrategy strategy = factory.criarEstrategia(600);

        assertNotNull(strategy);
        assertEquals("Aceitável", strategy.getDescricao());
    }

    @Test
    void deveCriarEstrategiaRecomendavel() {
        when(recomendavelStrategy.aplicavel(850)).thenReturn(true);
        when(recomendavelStrategy.getDescricao()).thenReturn("Recomendável");

        ScoreClassificacaoStrategy strategy = factory.criarEstrategia(850);

        assertNotNull(strategy);
        assertEquals("Recomendável", strategy.getDescricao());
    }

    @Test
    void deveRetornarErroParaScoreInvalido() {

        when(insuficienteStrategy.aplicavel(-1)).thenReturn(false);
        when(inaceitavelStrategy.aplicavel(-1)).thenReturn(false);
        when(aceitavelStrategy.aplicavel(-1)).thenReturn(false);
        when(recomendavelStrategy.aplicavel(-1)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            factory.criarEstrategia(-1);
        });
    }
}
