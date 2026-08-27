package com.senai.projetoCantina.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senai.projetoCantina.exception.RecursoNaoEncontradoException;
import com.senai.projetoCantina.model.Cliente;
import com.senai.projetoCantina.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Cliente cadastrar(Cliente cliente) {
        if (cliente.getMatricula() != null && !cliente.getMatricula().isBlank()) {
            if (clienteRepository.findByMatricula(cliente.getMatricula()).isPresent()) {
                throw new IllegalStateException("Já existe um cliente cadastrado com essa matrícula");
            }
        }
        return clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado. ID: " + id));
    }

    @Transactional
    public Cliente atualizar(Long id, Cliente dadosNovos) {
        Cliente existente = buscarPorId(id);
        if (dadosNovos.getMatricula() != null && !dadosNovos.getMatricula().isBlank()) {
            Optional<Cliente> clienteComMesmaMatricula = clienteRepository.findByMatricula(dadosNovos.getMatricula());
            if (clienteComMesmaMatricula.isPresent() && !clienteComMesmaMatricula.get().getId().equals(id)) {
                throw new IllegalStateException("Já existe outro cliente cadastrado com essa matrícula");
            }
        }
        
        existente.setNome(dadosNovos.getNome());
        existente.setMatricula(dadosNovos.getMatricula());
        existente.setIdTipoCliente(dadosNovos.getIdTipoCliente());

        return clienteRepository.save(existente);
    }

    @Transactional
    public void excluir(Long id) {
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorMatricula(String matricula) {
        return clienteRepository.findByMatricula(matricula);
    }
}