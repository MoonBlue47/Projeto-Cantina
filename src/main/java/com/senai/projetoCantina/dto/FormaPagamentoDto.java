package com.senai.projetoCantina.dto;

import com.senai.projetoCantina.model.FormaPagamento;


public class FormaPagamentoDto {
	private Long id;
    private String tipo;

    public FormaPagamentoDto() {
    }

    public FormaPagamentoDto(Long id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

  
    public FormaPagamentoDto(FormaPagamento entity) {
        this.id = entity.getId();
        this.tipo = entity.getTipo() != null ? entity.getTipo().getDescricao() : null;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
    
    
}
