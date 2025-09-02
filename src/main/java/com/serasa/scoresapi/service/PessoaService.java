package com.serasa.scoresapi.service;

import com.serasa.scoresapi.dto.request.PessoaRequest;
import com.serasa.scoresapi.dto.response.PessoaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PessoaService {
    
    Page<PessoaResponse> listarTodos(Pageable pageable);
    
    Page<PessoaResponse> listarComFiltros(String nome, Integer idade, String cep, Pageable pageable);
    
    PessoaResponse buscarPorId(Long id);
    
    PessoaResponse criar(PessoaRequest request);
    
    PessoaResponse atualizar(Long id, PessoaRequest request);
    
    void deletar(Long id);
}
