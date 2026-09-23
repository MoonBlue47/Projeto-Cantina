package com.senai.projetoCantina.model;

import jakarta.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "categoria")
public class Categoria {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_categoria")
   private Long id;
   private String nome;
   private String descricao;

	public Categoria() {
		
	}
	//=============================
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	//=============================
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	//=============================
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	//=============================
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Categoria categoria = (Categoria) o;
		return id != null && id.equals(categoria.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
}
