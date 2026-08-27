package com.senai.projetoCantina.repository;

import com.senai.projetoCantina.model.VendaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendaPagamentoRepository extends JpaRepository<VendaPagamento, Long> {

    List<VendaPagamento> findByVendaId(Long idVenda);
}