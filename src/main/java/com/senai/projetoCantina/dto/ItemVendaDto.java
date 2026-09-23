package com.senai.projetoCantina.dto;

import java.math.*;

import com.senai.projetoCantina.model.ItemVenda;

public class ItemVendaDto {
	private Long id;
    private Long idProduto;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
    
    public ItemVendaDto() {
    	}
    
    public ItemVendaDto(Long id, Long idProduto, Integer quantidade, BigDecimal precoUnitario, BigDecimal subtotal) {
        this.id = id;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = subtotal;
    }
    public ItemVendaDto(ItemVenda entity) {
    	this.id =entity.getId();
    	this.idProduto = entity.getIdProduto();
    	this.precoUnitario = entity.getPrecoUnitario();
    	this.quantidade = entity.getQuantidade();
    	this.subtotal = entity.getSubtotal();
    	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(BigDecimal precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
    
    
}
