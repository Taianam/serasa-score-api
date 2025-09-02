package com.serasa.scoresapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.serasa.scoresapi.dto.request.UserRequest;
import com.serasa.scoresapi.dto.response.UserResponse;

public interface UserService {
    
    UserResponse criar(UserRequest request);
    
    Page<UserResponse> listarTodos(Pageable pageable);
    
    UserResponse buscarPorId(Long id);
    
    UserResponse atualizar(Long id, UserRequest request);
    
    void deletar(Long id);
    
    UserResponse ativar(Long id);
    
    UserResponse desativar(Long id);
}
