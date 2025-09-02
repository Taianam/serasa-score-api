package com.serasa.scoresapi.domain.factory;

import com.serasa.scoresapi.domain.strategy.ScoreClassificacaoStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScoreClassificacaoFactory {
    
    private final List<ScoreClassificacaoStrategy> strategies;
    
    public ScoreClassificacaoFactory(List<ScoreClassificacaoStrategy> strategies) {
        this.strategies = strategies;
    }
    
    public ScoreClassificacaoStrategy criarEstrategia(int score) {
        return strategies.stream()
                .filter(strategy -> strategy.aplicavel(score))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Score inválido: " + score));
    }
}
