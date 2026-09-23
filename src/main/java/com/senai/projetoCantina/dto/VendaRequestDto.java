package com.senai.projetoCantina.dto;

import java.util.ArrayList;
import java.util.List;

public class VendaRequestDto {
	private Long idCliente;
    private Long idFuncionario;
    private List<ItemVendaDto> itens = new ArrayList<>();
    private List<VendaPagamentoDto> pagamentos = new ArrayList<>();
    
    public VendaRequestDto() {
    	
    }
    
    public VendaRequestDto(Long idClient, Long idFuncionario,List<ItemVendaDto> itens,List<VendaPagamentoDto> pagamentos) {
    	this.idCliente=idClient;
    	this.idFuncionario =idFuncionario;
    	this.itens =itens;
    	this.pagamentos =pagamentos;
    	
    }

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getIdFuncionario() {
		return idFuncionario;
	}

	public void setIdFuncionario(Long idFuncionario) {
		this.idFuncionario = idFuncionario;
	}

	public List<ItemVendaDto> getItens() {
		return itens;
	}

	public void setItens(List<ItemVendaDto> itens) {
		this.itens = itens;
	}

	public List<VendaPagamentoDto> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<VendaPagamentoDto> pagamentos) {
		this.pagamentos = pagamentos;
	}
    
}
