package com.serasa.scoresapi.domain.strategy.impl;

import com.serasa.scoresapi.domain.strategy.ScoreClassificacaoStrategy;
import org.springframework.stereotype.Component;

@Component
public class ScoreRecomendavelStrategy implements ScoreClassificacaoStrategy {
    
    @Override
    public String getDescricao() {
        return "Recomendável";
    }
    
    @Override
    public boolean aplicavel(int score) {
        return score >= 701 && score <= 1000;
    }
}
