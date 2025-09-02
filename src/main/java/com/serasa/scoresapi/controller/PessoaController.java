package com.serasa.scoresapi.controller;

import com.serasa.scoresapi.dto.request.PessoaRequest;
import com.serasa.scoresapi.dto.response.PessoaResponse;
import com.serasa.scoresapi.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pessoas")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Pessoas", description = "Operações relacionadas a pessoas")
public class PessoaController {
    
    private final PessoaService pessoaService;
    
    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Listar pessoas", description = "Lista todas as pessoas com paginação")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de pessoas retornada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<PessoaResponse>> listarTodos(Pageable pageable) {
        return ResponseEntity.ok(pessoaService.listarTodos(pageable));
    }
    
    @GetMapping("/paginado")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Listar pessoas com filtros", description = "Lista pessoas com filtros opcionais")
    public ResponseEntity<Page<PessoaResponse>> listarComFiltros(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer idade,
            @RequestParam(required = false) String cep,
            Pageable pageable) {
        return ResponseEntity.ok(pessoaService.listarComFiltros(nome, idade, cep, pageable));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar pessoa", description = "Cria uma nova pessoa")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pessoa criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<PessoaResponse> criar(@Valid @RequestBody PessoaRequest request) {
        PessoaResponse response = pessoaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Buscar pessoa por ID", description = "Busca uma pessoa específica pelo ID")
    public ResponseEntity<PessoaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pessoaService.buscarPorId(id));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar pessoa", description = "Atualiza os dados de uma pessoa")
    public ResponseEntity<PessoaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody PessoaRequest request) {
        return ResponseEntity.ok(pessoaService.atualizar(id, request));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar pessoa", description = "Remove uma pessoa (soft delete)")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pessoaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
