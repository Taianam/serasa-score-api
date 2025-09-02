package com.serasa.scoresapi.repository;

import com.serasa.scoresapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsernameAndAtivoTrue(String username);
    
    boolean existsByUsernameAndAtivoTrue(String username);
    
    boolean existsByUsername(String username);
}
