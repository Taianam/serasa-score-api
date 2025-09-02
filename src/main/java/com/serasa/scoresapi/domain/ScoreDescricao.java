package com.serasa.scoresapi.domain;

public enum ScoreDescricao {
    INSUFICIENTE("Insuficiente", 0, 200),
    INACEITAVEL("Inaceitável", 201, 500),
    ACEITAVEL("Aceitável", 501, 700),
    RECOMENDAVEL("Recomendável", 701, 1000);

    private final String descricao;
    private final int valorInicial;
    private final int valorFinal;

    ScoreDescricao(String descricao, int valorInicial, int valorFinal) {
        this.descricao = descricao;
        this.valorInicial = valorInicial;
        this.valorFinal = valorFinal;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getValorInicial() {
        return valorInicial;
    }

    public int getValorFinal() {
        return valorFinal;
    }

    public static String getDescricao(int score) {
        if (score < 0 || score > 1000) {
            return "Score inválido";
        }

        for (ScoreDescricao descricao : values()) {
            if (score >= descricao.valorInicial && score <= descricao.valorFinal) {
                return descricao.descricao;
            }
        }

        return "Score inválido";
    }
}
