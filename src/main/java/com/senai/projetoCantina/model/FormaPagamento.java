package com.senai.projetoCantina.model;

import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_forma_pagamento")
public class FormaPagamento {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 30)
    private TipoPagamento tipo;

    public FormaPagamento() {
    }

    public FormaPagamento(Long id, TipoPagamento tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoPagamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoPagamento tipo) {
        this.tipo = tipo;
    }

 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormaPagamento that = (FormaPagamento) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }



    public enum TipoPagamento {
        DINHEIRO("Dinheiro"),
        PIX("Pix"),
        CARTAO_DEBITO("Cartão de Débito"),
        CARTAO_CREDITO("Cartão de Crédito"),
        VALE_REFEICAO("Vale Refeição");

        private final String descricao;

        TipoPagamento(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}