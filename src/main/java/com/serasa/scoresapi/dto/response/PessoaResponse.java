package com.serasa.scoresapi.dto.response;

public class PessoaResponse {
    
    private Long id;
    private String nome;
    private Integer idade;
    private String telefone;
    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String estado;
    private Integer score;
    private String descricaoScore;
    
    public PessoaResponse() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public Integer getIdade() { return idade; }
    public void setIdade(Integer idade) { this.idade = idade; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    
    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }
    
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    
    public String getDescricaoScore() { return descricaoScore; }
    public void setDescricaoScore(String descricaoScore) { this.descricaoScore = descricaoScore; }
}
