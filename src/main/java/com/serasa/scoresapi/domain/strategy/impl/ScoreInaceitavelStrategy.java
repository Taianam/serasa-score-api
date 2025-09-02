package com.serasa.scoresapi.domain.strategy.impl;

import com.serasa.scoresapi.domain.strategy.ScoreClassificacaoStrategy;
import org.springframework.stereotype.Component;

@Component
public class ScoreInaceitavelStrategy implements ScoreClassificacaoStrategy {
    
    @Override
    public String getDescricao() {
        return "Inaceitável";
    }
    
    @Override
    public boolean aplicavel(int score) {
        return score >= 201 && score <= 500;
    }
}
