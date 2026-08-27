package com.senai.projetoCantina.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.senai.projetoCantina.model.TipoCliente;
import com.senai.projetoCantina.repository.TipoClienteRepository;

import com.senai.projetoCantina.exception.*;

@Service
public class TipoClienteService {

    private final TipoClienteRepository tipoClienteRepository;

    public TipoClienteService(TipoClienteRepository tipoClienteRepository) {
        this.tipoClienteRepository = tipoClienteRepository;
    }

    @Transactional
    public TipoCliente cadastrar(TipoCliente tipoCliente) {
        if (tipoCliente.getNome() != null && !tipoCliente.getNome().isBlank()) {
            if (tipoClienteRepository.findByNome(tipoCliente.getNome()).isPresent()) {
                throw new IllegalStateException("Já existe um tipo de cliente com essa descrição");
            }
        }
        return tipoClienteRepository.save(tipoCliente);
    }

    @Transactional(readOnly = true)
    public List<TipoCliente> listarTodos() {
        return tipoClienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TipoCliente buscarPorId(Long id) {
        return tipoClienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TipoCliente não encontrado. ID: " + id));
    }

    @Transactional
    public TipoCliente atualizar(Long id, TipoCliente dadosNovos) {
        TipoCliente existente = buscarPorId(id);

        if (dadosNovos.getNome() != null && !dadosNovos.getNome().isBlank()) {
            Optional<TipoCliente> tipoComMesmaDescricao = tipoClienteRepository.findByNome(dadosNovos.getNome());
            if (tipoComMesmaDescricao.isPresent() && !tipoComMesmaDescricao.get().getId().equals(id)) {
                throw new IllegalStateException("Já existe outro tipo de cliente com essa descrição");
            }
        }

        existente.setNome(dadosNovos.getNome());
        return tipoClienteRepository.save(existente);
    }

    @Transactional
    public void excluir(Long id) {
        TipoCliente tipoCliente = buscarPorId(id);
        tipoClienteRepository.delete(tipoCliente);
    }
}