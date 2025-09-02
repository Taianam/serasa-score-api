package com.serasa.scoresapi.mapper;

import com.serasa.scoresapi.domain.Pessoa;
import com.serasa.scoresapi.dto.request.PessoaRequest;
import com.serasa.scoresapi.dto.response.PessoaResponse;
import com.serasa.scoresapi.service.ScoreClassificacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PessoaMapperTest {

    @Mock
    private ScoreClassificacaoService scoreClassificacaoService;

    private PessoaMapper pessoaMapper;
    private PessoaRequest pessoaRequest;
    private Pessoa pessoa;

    @BeforeEach
    void setUp() {
        pessoaMapper = new PessoaMapper(scoreClassificacaoService);

        pessoaRequest = new PessoaRequest();
        pessoaRequest.setNome("João Silva");
        pessoaRequest.setIdade(30);
        pessoaRequest.setCep("01310100");
        pessoaRequest.setTelefone("11999999999");
        pessoaRequest.setScore(750);

        pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("João Silva");
        pessoa.setIdade(30);
        pessoa.setCep("01310100");
        pessoa.setEstado("SP");
        pessoa.setCidade("São Paulo");
        pessoa.setBairro("Bela Vista");
        pessoa.setLogradouro("Avenida Paulista");
        pessoa.setTelefone("11999999999");
        pessoa.setScore(750);
        pessoa.setAtivo(true);
    }

    @Test
    void deveConverterPessoaRequestParaPessoa() {
        Pessoa resultado = pessoaMapper.toEntity(pessoaRequest);

        assertNotNull(resultado);
        assertEquals(pessoaRequest.getNome(), resultado.getNome());
        assertEquals(pessoaRequest.getIdade(), resultado.getIdade());
        assertEquals(pessoaRequest.getCep(), resultado.getCep());
        assertEquals(pessoaRequest.getTelefone(), resultado.getTelefone());
        assertEquals(pessoaRequest.getScore(), resultado.getScore());
    }

    @Test
    void deveConverterPessoaParaPessoaResponseComDescricaoScore() {
        when(scoreClassificacaoService.obterDescricaoScore(750)).thenReturn("Recomendável");
        
        PessoaResponse resultado = pessoaMapper.toResponse(pessoa);

        assertNotNull(resultado);
        assertEquals(pessoa.getId(), resultado.getId());
        assertEquals(pessoa.getNome(), resultado.getNome());
        assertEquals(pessoa.getIdade(), resultado.getIdade());
        assertEquals(pessoa.getCep(), resultado.getCep());
        assertEquals(pessoa.getEstado(), resultado.getEstado());
        assertEquals(pessoa.getCidade(), resultado.getCidade());
        assertEquals(pessoa.getBairro(), resultado.getBairro());
        assertEquals(pessoa.getLogradouro(), resultado.getLogradouro());
        assertEquals(pessoa.getTelefone(), resultado.getTelefone());
        
        assertEquals(pessoa.getScore(), resultado.getScore());
        assertEquals("Recomendável", resultado.getDescricaoScore());
    }

    @Test
    void deveConverterPessoaParaPessoaResponseSemScore() {
        pessoa.setScore(null);
        
        PessoaResponse resultado = pessoaMapper.toResponse(pessoa);

        assertNotNull(resultado);
        assertEquals(pessoa.getId(), resultado.getId());
        assertEquals(pessoa.getNome(), resultado.getNome());
        assertEquals(pessoa.getIdade(), resultado.getIdade());
        assertEquals(pessoa.getCep(), resultado.getCep());
        assertEquals(pessoa.getEstado(), resultado.getEstado());
        assertEquals(pessoa.getCidade(), resultado.getCidade());
        assertEquals(pessoa.getBairro(), resultado.getBairro());
        assertEquals(pessoa.getLogradouro(), resultado.getLogradouro());
        assertEquals(pessoa.getTelefone(), resultado.getTelefone());
        
        assertNull(resultado.getScore());
        assertNull(resultado.getDescricaoScore());
    }

    @Test
    void deveAtualizarPessoaComDadosDoRequest() {
        Pessoa pessoaExistente = new Pessoa();
        pessoaExistente.setId(1L);
        pessoaExistente.setNome("Nome Antigo");
        pessoaExistente.setIdade(25);
        pessoaExistente.setTelefone("11888888888");
        pessoaExistente.setScore(500);
        pessoaExistente.setCep("00000000");

        pessoaMapper.updateEntityFromRequest(pessoaRequest, pessoaExistente);

        assertEquals(pessoaRequest.getNome(), pessoaExistente.getNome());
        assertEquals(pessoaRequest.getIdade(), pessoaExistente.getIdade());
        assertEquals(pessoaRequest.getTelefone(), pessoaExistente.getTelefone());
        assertEquals(pessoaRequest.getScore(), pessoaExistente.getScore());
        assertEquals(pessoaRequest.getCep(), pessoaExistente.getCep());
        
        assertEquals(1L, pessoaExistente.getId());
    }

    @Test
    void deveRetornarNullQuandoPessoaRequestForNull() {
        Pessoa resultado = pessoaMapper.toEntity(null);
        assertNull(resultado);
    }

    @Test
    void deveRetornarNullQuandoPessoaForNull() {
        PessoaResponse resultado = pessoaMapper.toResponse(null);
        assertNull(resultado);
    }

    @Test
    void naoDeveAtualizarQuandoRequestForNull() {
        Pessoa pessoaOriginal = new Pessoa();
        pessoaOriginal.setNome("Nome Original");
        
        pessoaMapper.updateEntityFromRequest(null, pessoaOriginal);
        
        assertEquals("Nome Original", pessoaOriginal.getNome());
    }
}
