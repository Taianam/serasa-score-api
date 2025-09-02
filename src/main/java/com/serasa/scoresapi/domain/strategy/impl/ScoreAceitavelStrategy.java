package com.serasa.scoresapi.domain.strategy.impl;

import com.serasa.scoresapi.domain.strategy.ScoreClassificacaoStrategy;
import org.springframework.stereotype.Component;

@Component
public class ScoreAceitavelStrategy implements ScoreClassificacaoStrategy {
    
    @Override
    public String getDescricao() {
        return "Aceitável";
    }
    
    @Override
    public boolean aplicavel(int score) {
        return score >= 501 && score <= 700;
    }
}
