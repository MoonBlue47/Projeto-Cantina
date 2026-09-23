package com.senai.projetoCantina.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.senai.projetoCantina.model.TipoCliente;

@Repository
public interface TipoClienteRepository extends JpaRepository<TipoCliente, Long> {
    Optional<TipoCliente> findByNome(String nome);
}