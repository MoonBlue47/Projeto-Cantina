package com.senai.projetoCantina.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senai.projetoCantina.model.MovimentacaoEstoque;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    List<MovimentacaoEstoque> findByProdutoId(Long produtoId);

    Optional<MovimentacaoEstoque> findTopByProdutoIdOrderByDataMovimentacao(Long produtoId);
}
