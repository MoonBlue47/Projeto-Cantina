package com.senai.projetoCantina.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.senai.projetoCantina.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{
	Optional<Produto> findByNome(String nome);
	
	List<Produto> findByPrecoVendas(double precoVendas);
	List<Produto> findByCategoriaId(Long categoriaId);
}
