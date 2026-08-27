package com.senai.projetoCantina.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senai.projetoCantina.model.Compra;
import com.senai.projetoCantina.repository.CompraRepository;
import com.senai.projetoCantina.exception.*;

@Service
public class CompraService {

    private final CompraRepository compraRepository;

    public CompraService(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    @Transactional
    public Compra cadastrar(Compra compra) {
        return compraRepository.save(compra);
    }

    @Transactional(readOnly = true)
    public List<Compra> listarTodas() {
        return compraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Compra buscarPorId(Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Compra", id));
    }

    @Transactional
    public Compra atualizar(Long id, Compra dadosNovos) {
        Compra existente = buscarPorId(id);
        
        existente.setDataCompra(dadosNovos.getDataCompra());
        existente.setValorTotal(dadosNovos.getValorTotal());
        existente.setObservacao(dadosNovos.getObservacao());
        existente.setFuncionario(dadosNovos.getFuncionario());
        existente.setFornecedor(dadosNovos.getFornecedor());

        return compraRepository.save(existente);
    }

    @Transactional
    public void excluir(Long id) {
        Compra compra = buscarPorId(id);
        compraRepository.delete(compra);
    }
}