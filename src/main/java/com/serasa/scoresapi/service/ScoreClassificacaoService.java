package com.serasa.scoresapi.service;

import com.serasa.scoresapi.domain.strategy.ScoreClassificacaoStrategy;

public interface ScoreClassificacaoService {
    
    String obterDescricaoScore(int score);
    
    ScoreClassificacaoStrategy obterEstrategia(int score);
}
