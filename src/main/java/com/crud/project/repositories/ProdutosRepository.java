package com.crud.project.repositories;

import com.crud.project.models.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutosRepository extends JpaRepository<Produtos, Long> {
    List<Produtos> findByCategoria(String categoria);
    List<Produtos> findByFornecedorId(Long fornecedorId);
}
