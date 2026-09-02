package com.senai.projetoCantina.repository;

import com.senai.projetoCantina.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByIdCliente(Long idCliente);

    List<Venda> findByIdFuncionario(Long idFuncionario);

    List<Venda> findByStatus(Venda.StatusVenda status);
}