package com.serasa.scoresapi.dto.request;

import com.serasa.scoresapi.domain.Role;
import jakarta.validation.constraints.*;

public class UserRequest {
    
    @NotBlank(message = "Username é obrigatório")
    @Size(min = 3, max = 50, message = "Username deve ter entre 3 e 50 caracteres")
    private String username;
    
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String password;
    
    @NotNull(message = "Role é obrigatória")
    private Role role;
    

    public UserRequest() {}
    
    public UserRequest(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
