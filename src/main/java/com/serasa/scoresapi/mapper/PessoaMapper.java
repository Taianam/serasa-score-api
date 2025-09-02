package com.serasa.scoresapi.mapper;

import com.serasa.scoresapi.domain.Pessoa;
import com.serasa.scoresapi.dto.request.PessoaRequest;
import com.serasa.scoresapi.dto.response.PessoaResponse;
import com.serasa.scoresapi.service.ScoreClassificacaoService;
import org.springframework.stereotype.Component;

@Component
public class PessoaMapper {
    
    private final ScoreClassificacaoService scoreClassificacaoService;
    
    public PessoaMapper(ScoreClassificacaoService scoreClassificacaoService) {
        this.scoreClassificacaoService = scoreClassificacaoService;
    }
    
    public Pessoa toEntity(PessoaRequest request) {
        if (request == null) {
            return null;
        }
        
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(request.getNome());
        pessoa.setIdade(request.getIdade());
        pessoa.setTelefone(request.getTelefone());
        pessoa.setCep(request.getCep());
        pessoa.setScore(request.getScore());
        return pessoa;
    }
    
    public PessoaResponse toResponse(Pessoa pessoa) {
        if (pessoa == null) {
            return null;
        }
        
        PessoaResponse response = new PessoaResponse();
        response.setId(pessoa.getId());
        response.setNome(pessoa.getNome());
        response.setIdade(pessoa.getIdade());
        response.setTelefone(pessoa.getTelefone());
        response.setCep(pessoa.getCep());
        response.setLogradouro(pessoa.getLogradouro());
        response.setBairro(pessoa.getBairro());
        response.setCidade(pessoa.getCidade());
        response.setEstado(pessoa.getEstado());
        response.setScore(pessoa.getScore());
        

        if (pessoa.getScore() != null) {
            String descricao = scoreClassificacaoService.obterDescricaoScore(pessoa.getScore());
            response.setDescricaoScore(descricao);
        }
        
        return response;
    }
    
    public void updateEntityFromRequest(PessoaRequest request, Pessoa pessoa) {
        if (request == null || pessoa == null) {
            return;
        }
        
        pessoa.setNome(request.getNome());
        pessoa.setIdade(request.getIdade());
        pessoa.setTelefone(request.getTelefone());
        pessoa.setCep(request.getCep());
        pessoa.setScore(request.getScore());
    }
}
