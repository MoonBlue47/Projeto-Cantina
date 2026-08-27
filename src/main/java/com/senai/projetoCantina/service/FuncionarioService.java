package com.senai.projetoCantina.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.senai.projetoCantina.model.Funcionario;
import com.senai.projetoCantina.repository.FuncionarioRepository;
import com.senai.projetoCantina.exception.*;

@Service
public class FuncionarioService {

	private final FuncionarioRepository funcionarioRepository;

	public FuncionarioService(FuncionarioRepository funcionarioRepository) {
		this.funcionarioRepository = funcionarioRepository;
	}

	@Transactional
	public Funcionario cadastrar(Funcionario funcionario) {
		if (funcionarioRepository.findByCpf(funcionario.getCpf()).isPresent()) {
			throw new IllegalStateException("Já existe um funcionário cadastrado com esse CPF");
		}
		return funcionarioRepository.save(funcionario);
	}

	@Transactional(readOnly = true)
	public List<Funcionario> listarTodos() {
		return funcionarioRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Funcionario> buscarPorNome(String nome) {
		if (nome == null || nome.isBlank()) {
			return funcionarioRepository.findAll();
		}
		return funcionarioRepository.buscarPorNome(nome.trim());
	}

	@Transactional(readOnly = true)
	public Funcionario buscarPorId(Long id) {
		return funcionarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário", id));
	}

	@Transactional
	public Funcionario atualizar(Long id, Funcionario dadosNovos) {
		Funcionario existente = buscarPorId(id);
		existente.setNome(dadosNovos.getNome());
		existente.setCpf(dadosNovos.getCpf());
		existente.setCargo(dadosNovos.getCargo());
		existente.setAtivo(dadosNovos.getAtivo());
		return funcionarioRepository.save(existente);
	}

	@Transactional
	public void excluir(Long id) {
		Funcionario funcionario = buscarPorId(id);
		funcionarioRepository.delete(funcionario);
	}
}
