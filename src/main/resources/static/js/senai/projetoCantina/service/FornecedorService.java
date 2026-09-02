package com.senai.projetoCantina.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senai.projetoCantina.model.Fornecedor;
import com.senai.projetoCantina.repository.FornecedorRepository;

import com.senai.projetoCantina.exception.*;


@Service
public class FornecedorService {

	private final FornecedorRepository fornecedorRepository;

	public FornecedorService(FornecedorRepository fornecedorRepository) {
		this.fornecedorRepository = fornecedorRepository;
	}

	@Transactional
	public Fornecedor cadastrar(Fornecedor fornecedor) {
		if (fornecedorRepository.findByCnpj(fornecedor.getCnpj()).isPresent()) {
			throw new IllegalStateException("Já existe um fornecedor cadastrado com esse CNPJ");
		}
		return fornecedorRepository.save(fornecedor);
	}

	@Transactional(readOnly = true)
	public List<Fornecedor> listarTodos() {
		return fornecedorRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Fornecedor> buscarPorNome(String nome) {
		if (nome == null || nome.isBlank()) {
			return fornecedorRepository.findAll();
		}
		return fornecedorRepository.buscarPorNome(nome.trim());
	}

	@Transactional(readOnly = true)
    public Fornecedor buscarPorId(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado. ID: " + id));
    
	}

	@Transactional
	public Fornecedor atualizar(Long id, Fornecedor dadosNovos) {
		Fornecedor existente = buscarPorId(id);
		existente.setNome(dadosNovos.getNome());
		existente.setCnpj(dadosNovos.getCnpj());
		existente.setAtivo(dadosNovos.getAtivo());
		return fornecedorRepository.save(existente);
	}

	@Transactional
	public void excluir(Long id) {
		Fornecedor fornecedor = buscarPorId(id);
		fornecedorRepository.delete(fornecedor);
	}
}
