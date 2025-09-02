package com.serasa.scoresapi.domain.strategy;

public interface ScoreClassificacaoStrategy {
    String getDescricao();
    boolean aplicavel(int score);
}
