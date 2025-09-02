package com.serasa.scoresapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "scores")
public class Score {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Score é obrigatório")
    @Min(value = 0, message = "Score deve ser maior ou igual a 0")
    @Max(value = 1000, message = "Score deve ser menor ou igual a 1000")
    private Integer score;
    
    @Column(name = "data_registro")
    private LocalDateTime dataRegistro;
    
    @Column(length = 255)
    private String descricao;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;
    

    public Score() {
        this.dataRegistro = LocalDateTime.now();
    }
    
    public Score(Integer score, Pessoa pessoa) {
        this();
        this.score = score;
        this.pessoa = pessoa;
    }
    
    public Score(Integer score, String descricao, Pessoa pessoa) {
        this(score, pessoa);
        this.descricao = descricao;
    }
    

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    
    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public Pessoa getPessoa() { return pessoa; }
    public void setPessoa(Pessoa pessoa) { this.pessoa = pessoa; }
}
