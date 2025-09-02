package com.serasa.scoresapi.service.impl;

import com.serasa.scoresapi.domain.factory.ScoreClassificacaoFactory;
import com.serasa.scoresapi.domain.strategy.ScoreClassificacaoStrategy;
import com.serasa.scoresapi.service.ScoreClassificacaoService;
import org.springframework.stereotype.Service;

@Service
public class ScoreClassificacaoServiceImpl implements ScoreClassificacaoService {
    
    private final ScoreClassificacaoFactory factory;
    
    public ScoreClassificacaoServiceImpl(ScoreClassificacaoFactory factory) {
        this.factory = factory;
    }
    
    @Override
    public String obterDescricaoScore(int score) {
        try {
            ScoreClassificacaoStrategy strategy = factory.criarEstrategia(score);
            return strategy.getDescricao();
        } catch (IllegalArgumentException e) {
            return "Score inválido";
        }
    }
    
    @Override
    public ScoreClassificacaoStrategy obterEstrategia(int score) {
        return factory.criarEstrategia(score);
    }
}
