package com.senai.projetoCantina.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.senai.projetoCantina.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

	Optional<Funcionario> findByCpf(String cpf);

	List<Funcionario> findByAtivoTrue();

	@Query("SELECT f FROM Funcionario f WHERE LOWER(f.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
	List<Funcionario> buscarPorNome(@Param("nome") String nome);
}
