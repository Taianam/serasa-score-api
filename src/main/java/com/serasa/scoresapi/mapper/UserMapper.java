package com.serasa.scoresapi.mapper;

import com.serasa.scoresapi.domain.User;
import com.serasa.scoresapi.dto.request.UserRequest;
import com.serasa.scoresapi.dto.response.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    private final PasswordEncoder passwordEncoder;
    
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    public User toEntity(UserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setAtivo(true);
        return user;
    }
    
    public UserResponse toResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getRole(),
            user.getAtivo()
        );
    }
}
