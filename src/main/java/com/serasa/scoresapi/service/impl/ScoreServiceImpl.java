package com.serasa.scoresapi.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.serasa.scoresapi.domain.Pessoa;
import com.serasa.scoresapi.domain.Score;
import com.serasa.scoresapi.repository.PessoaRepository;
import com.serasa.scoresapi.repository.ScoreRepository;
import com.serasa.scoresapi.service.ScoreClassificacaoService;
import com.serasa.scoresapi.service.ScoreService;

@Service
@Transactional
public class ScoreServiceImpl implements ScoreService {
    
    private final ScoreRepository scoreRepository;
    private final PessoaRepository pessoaRepository;
    private final ScoreClassificacaoService scoreClassificacaoService;
    
    public ScoreServiceImpl(ScoreRepository scoreRepository, 
                           PessoaRepository pessoaRepository,
                           ScoreClassificacaoService scoreClassificacaoService) {
        this.scoreRepository = scoreRepository;
        this.pessoaRepository = pessoaRepository;
        this.scoreClassificacaoService = scoreClassificacaoService;
    }
     
    @Override
    @Transactional
    public void registrarScore(Long pessoaId, Integer score) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada"));
        

        String descricao = scoreClassificacaoService.obterDescricaoScore(score);
        
        Score novoScore = new Score(score, descricao, pessoa);
        scoreRepository.save(novoScore);
        

        pessoa.setScore(score);
        pessoaRepository.save(pessoa);
    }
}
