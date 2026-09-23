package com.senai.projetoCantina.dto;

import java.math.BigDecimal;

import com.senai.projetoCantina.model.VendaPagamento;

public class VendaPagamentoDto {
	private Long id;
	private Long idFormaPagamento;
    private String tipoFormaPagamento; 
    private BigDecimal valor;
    
    public VendaPagamentoDto() {
    	
    }
    public VendaPagamentoDto(Long id, Long idFormaPagamento,String tipoFormaPagamento,BigDecimal valor) {
    	this.id = id;
    	this.idFormaPagamento = idFormaPagamento;
    	this.tipoFormaPagamento = tipoFormaPagamento;
    	this.valor = valor;
    }
    public VendaPagamentoDto(VendaPagamento entity) {
    	this.id = entity.getId();
    	if (entity.getFormaPagamento() != null) {
            this.idFormaPagamento = entity.getFormaPagamento().getId();
            if (entity.getFormaPagamento().getTipo() != null) {
                this.tipoFormaPagamento = entity.getFormaPagamento().getTipo().getDescricao();
            }
        }
        this.valor = entity.getValorPago();
    }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdFormaPagamento() {
		return idFormaPagamento;
	}
	public void setIdFormaPagamento(Long idFormaPagamento) {
		this.idFormaPagamento = idFormaPagamento;
	}
	public String getTipoFormaPagamento() {
		return tipoFormaPagamento;
	}
	public void setTipoFormaPagamento(String tipoFormaPagamento) {
		this.tipoFormaPagamento = tipoFormaPagamento;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
    
}
