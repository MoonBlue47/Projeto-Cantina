package com.senai.projetoCantina.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senai.projetoCantina.model.Categoria;
import com.senai.projetoCantina.repository.CategoriaRepository;

@Service
public class CategoriaService {
	private final CategoriaRepository categoriaRepository;
	
	public CategoriaService(CategoriaRepository categoriaRepository) {
		this.categoriaRepository = categoriaRepository;
	}

	
	@Transactional
	public Categoria cadastrar(Categoria categoria) {
		categoria.setNome(categoria.getNome().trim().toLowerCase());
		if(categoriaRepository.findByNome(categoria.getNome()).isPresent()) {
			throw new IllegalStateException("Essa categoria já existe!");
		}
		return categoriaRepository.save(categoria);
	}
}
