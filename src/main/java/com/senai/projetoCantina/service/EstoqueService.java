package com.senai.projetoCantina.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senai.projetoCantina.model.Estoque;
import com.senai.projetoCantina.model.MovimentacaoEstoque;
import com.senai.projetoCantina.model.Produto;
import com.senai.projetoCantina.repository.EstoqueRepository;
import com.senai.projetoCantina.repository.MovimentacaoEstoqueRepository;

@Service
public class EstoqueService {
	private final EstoqueRepository estoqueRepository;
	private final MovimentacaoEstoqueRepository movimentacaoRepository;
	
	public EstoqueService(EstoqueRepository estoqueRepository, MovimentacaoEstoqueRepository movimentacaoRepository){
		this.estoqueRepository = estoqueRepository;
		this.movimentacaoRepository = movimentacaoRepository;
	}
	
	@Transactional
	public MovimentacaoEstoque registrarEntrada(Produto produto, int quantidade, String origem, double valorUnitario) {
		if(quantidade <= 0) {
			throw new IllegalStateException("A quantidade de entrada deve ser maior que zero");
		}
		
		MovimentacaoEstoque ultimaMovimentacao = movimentacaoRepository
				.findTopByProdutoIdOrderByDataMovimentacao(produto.getId())
				.orElse(null);
				
		int saldoAnterior = (ultimaMovimentacao != null) ? ultimaMovimentacao.getSaldoAtual(): 0;
		int saldoAtual = saldoAnterior + quantidade;
		
		MovimentacaoEstoque novaMovimentacao = new MovimentacaoEstoque();
		novaMovimentacao.setTipo("entrada");
		novaMovimentacao.setOrigem(origem);
		novaMovimentacao.setQuantidade(quantidade);
		novaMovimentacao.setSaldoAnterior(saldoAnterior);
		novaMovimentacao.setSaldoAtual(saldoAtual);
		novaMovimentacao.setDataMovimento(LocalDateTime.now());
		novaMovimentacao.setProduto(produto);
		
		movimentacaoRepository.saveAllAndFlush(null);
		
		atualizarPrateleira(produto, saldoAtual, valorUnitario, novaMovimentacao);
		
		return novaMovimentacao;
	}
	
	@Transactional
	public MovimentacaoEstoque registrarSaida(Produto produto, int quantidade, String origem) {
		if(quantidade <= 0) {
			throw new IllegalStateException("A quantidade de saida deve ser maior que zero");
		} 
		
		MovimentacaoEstoque ultimaMovimentacao = movimentacaoRepository
				.findTopByProdutoIdOrderByDataMovimentacao(produto.getId())
				.orElseThrow(() -> new IllegalStateException("Não há estoque cadastrado para este produto"));
		
		int saldoAnterior = ultimaMovimentacao.getSaldoAtual();
		
		if(saldoAnterior < quantidade) {
			throw new IllegalStateException("Estoque insuficiente! Saldo atual: " + saldoAnterior);
		}
		
		int saldoAtual = saldoAnterior - quantidade;
		
		MovimentacaoEstoque novaMovimentacao = new MovimentacaoEstoque();
		novaMovimentacao.setTipo("saida");
		novaMovimentacao.setOrigem(origem);
		novaMovimentacao.setQuantidade(quantidade);
		novaMovimentacao.setSaldoAnterior(saldoAnterior);
		novaMovimentacao.setSaldoAtual(saldoAtual);
		novaMovimentacao.setDataMovimento(LocalDateTime.now());
		novaMovimentacao.setProduto(produto);
		
		movimentacaoRepository.save(novaMovimentacao);
		
		atualizarPrateleira(produto, saldoAtual, 0, novaMovimentacao);
		
		return novaMovimentacao;
	}
		
	private void atualizarPrateleira(Produto produto, int saldoAtual, double valorUnitario, MovimentacaoEstoque ultimaMovimentacao) {
		
		Estoque estoque = estoqueRepository.findByProdutoId(produto.getId()).orElse(new Estoque());
		
		estoque.setProduto(produto);
		estoque.setQuantidade(saldoAtual);
		estoque.setMovimentacaoEstoque(ultimaMovimentacao);
		
		if(valorUnitario > 0) {
			estoque.setValorUnitario(valorUnitario);
		}
		estoque.setTotal(estoque.getQuantidade() * estoque.getValorUnitario());
		
		estoqueRepository.save(estoque);
	}
}