package com.senai.projetoCantina.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senai.projetoCantina.exception.RecursoNaoEncontradoException;
import com.senai.projetoCantina.model.Compra;
import com.senai.projetoCantina.model.ItemCompra;
import com.senai.projetoCantina.repository.CompraRepository;
import com.senai.projetoCantina.repository.ItemCompraRepository;


@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final ItemCompraRepository itemCompraRepository;
    private final EstoqueService estoqueService;

    public CompraService(CompraRepository compraRepository,
                         ItemCompraRepository itemCompraRepository,
                         EstoqueService estoqueService) {
        this.compraRepository = compraRepository;
        this.itemCompraRepository = itemCompraRepository;
        this.estoqueService = estoqueService;
    }

    @Transactional
    public Compra cadastrar(Compra compra) {
        Compra salva = compraRepository.save(compra);

        List<ItemCompra> itens = itemCompraRepository.findByCompraIdCompra(salva.getIdCompra());
        for (ItemCompra item : itens) {
            if (item.getProduto() != null && item.getQuantidade() != null && item.getQuantidade() > 0) {
                double valorUnitario = item.getPrecoUnitario() != null
                        ? item.getPrecoUnitario().doubleValue()
                        : 0.0;
                String origem = "Compra #" + salva.getIdCompra();
                estoqueService.registrarEntrada(item.getProduto(), item.getQuantidade(), origem, valorUnitario);
            }
        }

        return salva;
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