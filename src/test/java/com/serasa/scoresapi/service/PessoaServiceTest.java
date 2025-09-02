package com.serasa.scoresapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.serasa.scoresapi.domain.Pessoa;
import com.serasa.scoresapi.domain.client.EnderecoResponse;
import com.serasa.scoresapi.dto.request.PessoaRequest;
import com.serasa.scoresapi.dto.response.PessoaResponse;
import com.serasa.scoresapi.facade.EnderecoFacade;
import com.serasa.scoresapi.mapper.PessoaMapper;
import com.serasa.scoresapi.repository.PessoaRepository;
import com.serasa.scoresapi.service.impl.PessoaServiceImpl;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private PessoaMapper pessoaMapper;

    @Mock
    private EnderecoFacade enderecoFacade;

    @Mock
    private ScoreService scoreService;

    @InjectMocks
    private PessoaServiceImpl pessoaService;

    private PessoaRequest pessoaRequest;
    private Pessoa pessoa;
    private PessoaResponse pessoaResponse;

    @BeforeEach
    void setUp() {	
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
        pessoa.setTelefone("11999999999");
        pessoa.setScore(750);
        pessoa.setAtivo(true);
        pessoa.setEstado("SP");
        pessoa.setCidade("São Paulo");
        pessoa.setBairro("Bela Vista");
        pessoa.setLogradouro("Avenida Paulista");

        pessoaResponse = new PessoaResponse();
        pessoaResponse.setId(1L);
        pessoaResponse.setNome("João Silva");
        pessoaResponse.setIdade(30);
        pessoaResponse.setCep("01310100");
        pessoaResponse.setTelefone("11999999999");
        pessoaResponse.setScore(750);
        pessoaResponse.setDescricaoScore("Recomendável");
    }

    @Test
    void deveCriarPessoaComSucesso() {

        EnderecoResponse enderecoResponse = new EnderecoResponse();
        enderecoResponse.setLogradouro("Avenida Paulista");
        enderecoResponse.setBairro("Bela Vista");
        enderecoResponse.setLocalidade("São Paulo");
        enderecoResponse.setUf("SP");

        when(pessoaMapper.toEntity(any(PessoaRequest.class))).thenReturn(pessoa);
        when(enderecoFacade.buscarEnderecoPorCep(any())).thenReturn(enderecoResponse);
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);
        when(pessoaMapper.toResponse(any(Pessoa.class))).thenReturn(pessoaResponse);

        PessoaResponse response = pessoaService.criar(pessoaRequest);

        assertNotNull(response);
        assertEquals(pessoaRequest.getNome(), response.getNome());
        verify(pessoaRepository).save(any(Pessoa.class));
        verify(enderecoFacade).buscarEnderecoPorCep(pessoaRequest.getCep());
        verify(scoreService).registrarScore(1L, 750);
    }

    @Test
    void deveListarTodasPessoasComPaginacao() {
        List<Pessoa> pessoas = Arrays.asList(pessoa);
        Page<Pessoa> pessoaPage = new PageImpl<>(pessoas);
        Pageable pageable = PageRequest.of(0, 10);

        when(pessoaRepository.findAll(pageable)).thenReturn(pessoaPage);
        when(pessoaMapper.toResponse(any(Pessoa.class))).thenReturn(pessoaResponse);

        Page<PessoaResponse> responses = pessoaService.listarTodos(pageable);

        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.getTotalElements());
        verify(pessoaRepository).findAll(pageable);
    }

    @Test
    void deveListarPessoasComFiltros() {
        List<Pessoa> pessoas = Arrays.asList(pessoa);
        Page<Pessoa> pessoaPage = new PageImpl<>(pessoas);
        Pageable pageable = PageRequest.of(0, 10);

        when(pessoaRepository.findPessoasComFiltros("João", 30, "01310100", pageable)).thenReturn(pessoaPage);
        when(pessoaMapper.toResponse(any(Pessoa.class))).thenReturn(pessoaResponse);

        Page<PessoaResponse> responses = pessoaService.listarComFiltros("João", 30, "01310100", pageable);

        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.getTotalElements());
        verify(pessoaRepository).findPessoasComFiltros("João", 30, "01310100", pageable);
    }

    @Test
    void deveBuscarPessoaPorId() {
        when(pessoaRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(pessoa));
        when(pessoaMapper.toResponse(any(Pessoa.class))).thenReturn(pessoaResponse);

        PessoaResponse response = pessoaService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(pessoa.getId(), response.getId());
        assertEquals(pessoa.getNome(), response.getNome());
        verify(pessoaRepository).findByIdAndAtivoTrue(1L);
    }

    @Test
    void deveAtualizarPessoa() {
        EnderecoResponse enderecoResponse = new EnderecoResponse();
        enderecoResponse.setLogradouro("Avenida Paulista");
        enderecoResponse.setBairro("Bela Vista");
        enderecoResponse.setLocalidade("São Paulo");
        enderecoResponse.setUf("SP");

        when(pessoaRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);
        when(pessoaMapper.toResponse(any(Pessoa.class))).thenReturn(pessoaResponse);

        PessoaResponse response = pessoaService.atualizar(1L, pessoaRequest);

        assertNotNull(response);
        assertEquals(pessoaRequest.getNome(), response.getNome());
        verify(pessoaRepository).save(any(Pessoa.class));
    }

    @Test
    void deveDeletarPessoaLogicamente() {
        when(pessoaRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        pessoaService.deletar(1L);

        verify(pessoaRepository).save(any(Pessoa.class));
        assertFalse(pessoa.getAtivo());
    }
}
