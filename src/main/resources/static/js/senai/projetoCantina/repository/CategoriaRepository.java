package com.senai.projetoCantina.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.senai.projetoCantina.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
	Optional<Categoria> findByNome(String nome);
	
	@Query("SELECT c FROM Categoria c WHERE LOWER(c.nome) = LOWER(:nome)")
	List<Categoria>buscarPorNome(@Param("nome")String nome);
}
