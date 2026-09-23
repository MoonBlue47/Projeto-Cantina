package com.senai.projetoCantina.model;

import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_cliente")
public class Cliente {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "id_tipo_cliente", nullable = false)
	private Long idTipoCliente;
	
	@Column(nullable = false, length = 100)
	private String nome;
	
	@Column(nullable = false, unique = true, length = 50)
	private String matricula;
	
	public Cliente() {
		
	}

	public Cliente(Long id,Long idTipoCliente,String nome,String matricula) {
		this.id= id;
		this.idTipoCliente =idTipoCliente;
		this.matricula=matricula;
		this.nome=nome;
		
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdTipoCliente() {
		return idTipoCliente;
	}

	public void setIdTipoCliente(Long idTipoCliente) {
		this.idTipoCliente = idTipoCliente;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Cliente cliente = (Cliente) o;
		return id != null && id.equals(cliente.id);
		
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
		
	}

}
