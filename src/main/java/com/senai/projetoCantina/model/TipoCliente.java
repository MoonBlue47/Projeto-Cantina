package com.senai.projetoCantina.model;

import java.util.Objects;

import jakarta.persistence.*;


@Entity
@Table(name = "tipo_cliente")
public class TipoCliente {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tipo_cliente")
	private Long id;
	
	@Column(nullable = false, unique = true, length = 50)
	private String nome;
	
	public TipoCliente() {
		
	}
	
	public TipoCliente(Long id,String nome) {
		this.id=id;
		this.nome=nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoCliente that = (TipoCliente) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
