package com.serasa.scoresapi.domain.strategy.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreInsuficienteStrategyTest {

    private ScoreInsuficienteStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new ScoreInsuficienteStrategy();
    }

    @Test
    void deveSerAplicavelParaScoreZero() {
        assertTrue(strategy.aplicavel(0));
    }

    @Test
    void deveSerAplicavelParaScore200() {
        assertTrue(strategy.aplicavel(200));
    }

    @Test
    void deveSerAplicavelParaScoreDentroDoRange() {
        assertTrue(strategy.aplicavel(100));
        assertTrue(strategy.aplicavel(150));
    }

    @Test
    void naoDeveSerAplicavelParaScore201() {
        assertFalse(strategy.aplicavel(201));
    }

    @Test
    void naoDeveSerAplicavelParaScoreAcimaDe200() {
        assertFalse(strategy.aplicavel(250));
        assertFalse(strategy.aplicavel(500));
        assertFalse(strategy.aplicavel(1000));
    }

    @Test
    void naoDeveSerAplicavelParaScoreNegativo() {
        assertFalse(strategy.aplicavel(-1));
        assertFalse(strategy.aplicavel(-100));
    }

    @Test
    void deveRetornarDescricaoCorreta() {
        assertEquals("Insuficiente", strategy.getDescricao());
    }
}
