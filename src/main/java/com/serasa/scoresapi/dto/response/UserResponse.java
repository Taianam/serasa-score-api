package com.serasa.scoresapi.dto.response;

import com.serasa.scoresapi.domain.Role;

public class UserResponse {
    
    private Long id;
    private String username;
    private Role role;
    private Boolean ativo;
    

    public UserResponse() {}
    
    public UserResponse(Long id, String username, Role role, Boolean ativo) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.ativo = ativo;
    }
    

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
