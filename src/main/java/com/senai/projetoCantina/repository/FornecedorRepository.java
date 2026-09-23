package com.senai.projetoCantina.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.senai.projetoCantina.model.Fornecedor;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

	Optional<Fornecedor> findByCnpj(String cnpj);

	List<Fornecedor> findByAtivoTrue();

	@Query("SELECT f FROM Fornecedor f WHERE LOWER(f.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
	List<Fornecedor> buscarPorNome(@Param("nome") String nome);
}
