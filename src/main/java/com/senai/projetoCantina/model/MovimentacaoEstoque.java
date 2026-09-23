package com.senai.projetoCantina.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "movimentacao_estoque")
public class MovimentacaoEstoque {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_movimentacao_estoque")
	private Long id;
	private String tipo;
	private String origem;
	private int quantidade;
	@Column(name = "saldo_anterior")
	private int saldoAnterior;
	@Column(name = "saldo_atual")
	private int saldoAtual;
	@Column(name = "data_movimento")
	private LocalDateTime dataMovimento;
	@ManyToOne
	@JoinColumn(name = "id_produto")
	private Produto produto;
	
	
	public MovimentacaoEstoque() {
		
	}
	//=============================
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	//=============================
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	//=============================
	public String getOrigem() {
		return origem;
	}
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	//=============================
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	//=============================
	public int getSaldoAnterior() {
		return saldoAnterior;
	}
	public void setSaldoAnterior(int saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	}
	//=============================
	public int getSaldoAtual() {
		return saldoAtual;
	}
	public void setSaldoAtual(int saldoAtual) {
		this.saldoAtual = saldoAtual;
	}
	//=============================
	public LocalDateTime getDataMovimento() {
		return dataMovimento;
	}
	public void setDataMovimento(LocalDateTime dataMovimento) {
		this.dataMovimento = dataMovimento;
	}
	//=============================
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	//=============================
	@Override
	public boolean equals(Object o) {
		if(this == o)return true;
		if(o == null||getClass()!= o.getClass())return false;
		MovimentacaoEstoque movimentacaoEstoque = (MovimentacaoEstoque) o;
		return id != null && id.equals(movimentacaoEstoque.id);
	}
	@Override 
	public int hashCode() {
		return Objects.hash(getClass());
	}
	
	
}
