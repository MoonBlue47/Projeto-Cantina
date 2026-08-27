package com.senai.projetoCantina.model;

import jakarta.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "estoque")
public class Estoque {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_estoque")
	private Long id;
	private int quantidade;
	@Column(name = "valor_unitario")
	private double valorUnitario;
	private double total;
	@ManyToOne
	@JoinColumn(name = "id_produto")
	private Produto produto;
	@ManyToOne
	@JoinColumn(name = "id_movimentacao_estoque")
	private MovimentacaoEstoque movimentacaoEstoque;
	
	public Estoque() {
		
	}
	//=============================
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	//=============================
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	//=============================
	public double getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	//=============================
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	//=============================
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	//=============================
	public MovimentacaoEstoque getMovimentacaoEstoque() {
		return movimentacaoEstoque;
	}
	public void setMovimentacaoEstoque(MovimentacaoEstoque movimentacaoEstoque) {
		this.movimentacaoEstoque = movimentacaoEstoque;
	}
	//=============================
	@Override
	public boolean equals(Object o) {
		if(this == o)return true;
		if(o == null||getClass()!= o.getClass())return false;
		Estoque estoque = (Estoque) o;
		return id != null && id.equals(estoque.id);
	}
	@Override 
	public int hashCode() {
		return Objects.hash(id);
	}
	
	

}
