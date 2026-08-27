package com.senai.projetoCantina.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.senai.projetoCantina.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Método auxiliar para buscar cliente por matrícula (usado nas validações do Service)
    Optional<Cliente> findByMatricula(String matricula);
}
