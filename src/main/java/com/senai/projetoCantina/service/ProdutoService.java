package com.senai.projetoCantina.service;

import org.springframework.stereotype.Service;
import com.senai.projetoCantina.model.Produto;
import com.senai.projetoCantina.repository.ProdutoRepository;
import jakarta.transaction.Transactional;

@Service
public class ProdutoService {
	private final ProdutoRepository produtoRepository;
	
	public ProdutoService(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}
	
	@Transactional
	public Produto cadastrar(Produto produto) {
		produto.setNome(produto.getNome().trim().toLowerCase());
		if(produtoRepository.findByNome(produto.getNome()).isPresent()) {
			throw new IllegalStateException("Esse produto já existe.");
		}
		//=====================================================================================
		if(produto.getPrecoVendas() <= 0) {
			throw new IllegalStateException("O preço do produto deve ser maior que zero!");
		}
		return produtoRepository.save(produto);
	}
	
}
