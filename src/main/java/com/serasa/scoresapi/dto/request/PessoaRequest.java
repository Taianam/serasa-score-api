package com.serasa.scoresapi.dto.request;

import jakarta.validation.constraints.*;

public class PessoaRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;
    
    @NotNull(message = "Idade é obrigatória")
    @Min(value = 0, message = "Idade deve ser maior ou igual a 0")
    @Max(value = 150, message = "Idade deve ser menor ou igual a 150")
    private Integer idade;
    
    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "^\\(?[1-9]{2}\\)? ?(?:[2-8]|9[1-9])[0-9]{3}-?[0-9]{4}$", 
             message = "Telefone deve estar no formato válido")
    private String telefone;
    
    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "CEP deve estar no formato válido")
    private String cep;
    
    @NotNull(message = "Score é obrigatório")
    @Min(value = 0, message = "Score deve ser maior ou igual a 0")
    @Max(value = 1000, message = "Score deve ser menor ou igual a 1000")
    private Integer score;
    

    public PessoaRequest() {}
    

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public Integer getIdade() { return idade; }
    public void setIdade(Integer idade) { this.idade = idade; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
}
