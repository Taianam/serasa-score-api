package com.serasa.scoresapi.service.impl;

import com.serasa.scoresapi.domain.User;
import com.serasa.scoresapi.dto.request.UserRequest;
import com.serasa.scoresapi.dto.response.UserResponse;
import com.serasa.scoresapi.mapper.UserMapper;
import com.serasa.scoresapi.repository.UserRepository;
import com.serasa.scoresapi.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public UserResponse criar(UserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username já existe: " + request.getUsername());
        }
        
        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> listarTodos(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserResponse buscarPorId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com ID: " + id));
        return userMapper.toResponse(user);
    }
    
    @Override
    public UserResponse atualizar(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com ID: " + id));
        
        if (!user.getUsername().equals(request.getUsername()) && 
            userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username já existe: " + request.getUsername());
        }
        
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }
    
    @Override
    public void deletar(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com ID: " + id));
        

        user.setAtivo(false);
        userRepository.save(user);
    }
    
    @Override
    public UserResponse ativar(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com ID: " + id));
        
        user.setAtivo(true);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }
    
    @Override
    public UserResponse desativar(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com ID: " + id));
        
        user.setAtivo(false);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }
}
