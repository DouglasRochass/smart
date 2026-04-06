package com.crud.project.repositories;

import com.crud.project.models.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    Optional<Fornecedor> findByNome(String nome);
    Optional<Fornecedor> findByEmail(String email);
}
