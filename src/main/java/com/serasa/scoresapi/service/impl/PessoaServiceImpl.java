package com.serasa.scoresapi.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.serasa.scoresapi.domain.Pessoa;
import com.serasa.scoresapi.dto.request.PessoaRequest;
import com.serasa.scoresapi.dto.response.PessoaResponse;
import com.serasa.scoresapi.facade.EnderecoFacade;
import com.serasa.scoresapi.mapper.PessoaMapper;
import com.serasa.scoresapi.repository.PessoaRepository;
import com.serasa.scoresapi.service.PessoaService;
import com.serasa.scoresapi.service.ScoreService;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class PessoaServiceImpl implements PessoaService {
    
    private final PessoaRepository pessoaRepository;
    private final PessoaMapper pessoaMapper;
    private final EnderecoFacade enderecoFacade;
    private final ScoreService scoreService;
    
    public PessoaServiceImpl(PessoaRepository pessoaRepository,
                            PessoaMapper pessoaMapper,
                            EnderecoFacade enderecoFacade,
                            ScoreService scoreService) {
        this.pessoaRepository = pessoaRepository;
        this.pessoaMapper = pessoaMapper;
        this.enderecoFacade = enderecoFacade;
        this.scoreService = scoreService;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PessoaResponse> listarTodos(Pageable pageable) {
        return pessoaRepository.findAll(pageable)
                .map(pessoa -> pessoaMapper.toResponse(pessoa));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PessoaResponse> listarComFiltros(String nome, Integer idade, String cep, Pageable pageable) {
        return pessoaRepository.findPessoasComFiltros(nome, idade, cep, pageable)
                .map(pessoa -> pessoaMapper.toResponse(pessoa));
    }
    
    @Override
    @Transactional(readOnly = true)
    public PessoaResponse buscarPorId(Long id) {
        Pessoa pessoa = pessoaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com ID: " + id));
        return pessoaMapper.toResponse(pessoa);
    }
    
    @Override
    @Transactional
    public PessoaResponse criar(PessoaRequest request) {
        Pessoa pessoa = pessoaMapper.toEntity(request);
        pessoa.setAtivo(true);
        

        var endereco = enderecoFacade.buscarEnderecoPorCep(request.getCep());
        pessoa.setLogradouro(endereco.getLogradouro());
        pessoa.setBairro(endereco.getBairro());
        pessoa.setCidade(endereco.getLocalidade());
        pessoa.setEstado(endereco.getUf());
        
        pessoa = pessoaRepository.save(pessoa);
        

        scoreService.registrarScore(pessoa.getId(), request.getScore());
        
        return pessoaMapper.toResponse(pessoa);
    }
    
    @Override
    @Transactional
    public PessoaResponse atualizar(Long id, PessoaRequest request) {
        Pessoa pessoa = pessoaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com ID: " + id));
        
        pessoaMapper.updateEntityFromRequest(request, pessoa);
        
        if (!pessoa.getCep().equals(request.getCep())) {
            var endereco = enderecoFacade.buscarEnderecoPorCep(request.getCep());
            pessoa.setLogradouro(endereco.getLogradouro());
            pessoa.setBairro(endereco.getBairro());
            pessoa.setCidade(endereco.getLocalidade());
            pessoa.setEstado(endereco.getUf());
        }
        
        pessoa = pessoaRepository.save(pessoa);
        

        if (!pessoa.getScore().equals(request.getScore())) {
            scoreService.registrarScore(pessoa.getId(), request.getScore());
        }
        
        return pessoaMapper.toResponse(pessoa);
    }
    
    @Override
    @Transactional
    public void deletar(Long id) {
        Pessoa pessoa = pessoaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com ID: " + id));
        pessoa.setAtivo(false);
        pessoaRepository.save(pessoa);
    }
}
