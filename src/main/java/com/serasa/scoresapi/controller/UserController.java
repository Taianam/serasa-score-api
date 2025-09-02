package com.serasa.scoresapi.controller;

import com.serasa.scoresapi.dto.request.UserRequest;
import com.serasa.scoresapi.dto.response.UserResponse;
import com.serasa.scoresapi.service.UserService;
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
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Usuários", description = "Operações relacionadas a usuários de autenticação")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar usuário", description = "Cria um novo usuário de autenticação (ADMIN apenas)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou username já existe"),
        @ApiResponse(responseCode = "403", description = "Acesso negado - apenas ADMIN")
    })
    public ResponseEntity<UserResponse> criar(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar usuários", description = "Lista todos os usuários de autenticação (ADMIN apenas)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado - apenas ADMIN")
    })
    public ResponseEntity<Page<UserResponse>> listarTodos(Pageable pageable) {
        return ResponseEntity.ok(userService.listarTodos(pageable));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar usuário por ID", description = "Busca um usuário específico pelo ID (ADMIN apenas)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado - apenas ADMIN")
    })
    public ResponseEntity<UserResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(userService.buscarPorId(id));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário (ADMIN apenas)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou username já existe"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado - apenas ADMIN")
    })
    public ResponseEntity<UserResponse> atualizar(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.atualizar(id, request));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar usuário", description = "Remove um usuário (soft delete - ADMIN apenas)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado - apenas ADMIN")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        userService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/ativar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Ativar usuário", description = "Ativa um usuário (ADMIN apenas)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário ativado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado - apenas ADMIN")
    })
    public ResponseEntity<UserResponse> ativar(@PathVariable Long id) {
        return ResponseEntity.ok(userService.ativar(id));
    }
    
    @PutMapping("/{id}/desativar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desativar usuário", description = "Desativa um usuário (ADMIN apenas)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário desativado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado - apenas ADMIN")
    })
    public ResponseEntity<UserResponse> desativar(@PathVariable Long id) {
        return ResponseEntity.ok(userService.desativar(id));
    }
}
