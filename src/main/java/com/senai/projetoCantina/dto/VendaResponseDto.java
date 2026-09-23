package com.senai.projetoCantina.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.senai.projetoCantina.model.Venda;


public class VendaResponseDto {
	private Long id;
    private LocalDateTime dataVenda;
    private BigDecimal valorTotal;
    private String status;
    private Long idCliente;
    private Long idFuncionario;
    private List<ItemVendaDto> itens = new ArrayList<>();
    private List<VendaPagamentoDto> pagamentos = new ArrayList<>();
    
    public VendaResponseDto() {
    	
    }
    
    public VendaResponseDto(Venda entity) {
    	this.id =entity.getId();
    	this.dataVenda=entity.getDataVenda();
    	this.valorTotal=entity.getValorTotal();
    	this.status=entity.getStatus()!=null ? entity.getStatus().getValorBanco():null;
    	this.idCliente=entity.getIdCliente();
    	this.idFuncionario=entity.getIdFuncionario();
    	if (entity.getItens() != null) {
            this.itens = entity.getItens().stream()
                    .map(ItemVendaDto::new)
                    .collect(Collectors.toList());
        }
        
        if (entity.getPagamentos() != null) {
            this.pagamentos = entity.getPagamentos().stream()
                    .map(VendaPagamentoDto::new)
                    .collect(Collectors.toList());
        }
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(LocalDateTime dataVenda) {
		this.dataVenda = dataVenda;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
