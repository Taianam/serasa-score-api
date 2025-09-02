package com.serasa.scoresapi.domain.strategy.impl;

import com.serasa.scoresapi.domain.strategy.ScoreClassificacaoStrategy;
import org.springframework.stereotype.Component;

@Component
public class ScoreInsuficienteStrategy implements ScoreClassificacaoStrategy {
    
    @Override
    public String getDescricao() {
        return "Insuficiente";
    }
    
    @Override
    public boolean aplicavel(int score) {
        return score >= 0 && score <= 200;
    }
}
