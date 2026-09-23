package com.senai.projetoCantina.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senai.projetoCantina.model.Estoque;

public interface EstoqueRepository extends JpaRepository<Estoque, Long>{
	Optional<Estoque> findByProdutoId(Long produtoid);
}
