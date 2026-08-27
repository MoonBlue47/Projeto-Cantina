package com.senai.projetoCantina.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.senai.projetoCantina.model.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
}