package com.serasa.scoresapi.repository;

import com.serasa.scoresapi.domain.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    
    Optional<Pessoa> findByIdAndAtivoTrue(Long id);
    
    @Query("SELECT p FROM Pessoa p WHERE p.ativo = true " +
           "AND (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
           "AND (:idade IS NULL OR p.idade = :idade) " +
           "AND (:cep IS NULL OR p.cep = :cep)")
    Page<Pessoa> findPessoasComFiltros(@Param("nome") String nome, 
                                       @Param("idade") Integer idade, 
                                       @Param("cep") String cep, 
                                       Pageable pageable);
}
