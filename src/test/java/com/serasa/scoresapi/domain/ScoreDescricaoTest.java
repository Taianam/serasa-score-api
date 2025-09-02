package com.serasa.scoresapi.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ScoreDescricaoTest {

    @ParameterizedTest
    @CsvSource({
        "0, Insuficiente",
        "100, Insuficiente", 
        "200, Insuficiente",
        "201, Inaceitável",
        "350, Inaceitável",
        "500, Inaceitável",
        "501, Aceitável",
        "600, Aceitável",
        "700, Aceitável",
        "701, Recomendável",
        "850, Recomendável",
        "1000, Recomendável"
    })
    void deveRetornarDescricaoCorretaParaScoreValido(int score, String descricaoEsperada) {
        String descricao = ScoreDescricao.getDescricao(score);
        assertEquals(descricaoEsperada, descricao);
    }

    @Test
    void deveRetornarInsuficienteParaScoreMinimo() {
        assertEquals("Insuficiente", ScoreDescricao.getDescricao(0));
    }

    @Test
    void deveRetornarRecomendavelParaScoreMaximo() {
        assertEquals("Recomendável", ScoreDescricao.getDescricao(1000));
    }

    @Test
    void deveRetornarScoreInvalidoParaValorNegativo() {
        assertEquals("Score inválido", ScoreDescricao.getDescricao(-1));
        assertEquals("Score inválido", ScoreDescricao.getDescricao(-100));
    }

    @Test
    void deveRetornarScoreInvalidoParaValorAcimaDoMaximo() {
        assertEquals("Score inválido", ScoreDescricao.getDescricao(1001));
        assertEquals("Score inválido", ScoreDescricao.getDescricao(1500));
    }

    @Test
    void deveTestarLimitesExatosDasFaixas() {

        assertEquals("Insuficiente", ScoreDescricao.getDescricao(200));
        assertEquals("Inaceitável", ScoreDescricao.getDescricao(201));
        
        assertEquals("Inaceitável", ScoreDescricao.getDescricao(500));
        assertEquals("Aceitável", ScoreDescricao.getDescricao(501));
        
        assertEquals("Aceitável", ScoreDescricao.getDescricao(700));
        assertEquals("Recomendável", ScoreDescricao.getDescricao(701));
    }

    @Test
    void deveVerificarTodasAsConstantesDoEnum() {

        ScoreDescricao[] valores = ScoreDescricao.values();
        
        assertEquals(4, valores.length);
        assertEquals(ScoreDescricao.INSUFICIENTE, valores[0]);
        assertEquals(ScoreDescricao.INACEITAVEL, valores[1]);
        assertEquals(ScoreDescricao.ACEITAVEL, valores[2]);
        assertEquals(ScoreDescricao.RECOMENDAVEL, valores[3]);
    }
}
