package com.senai.projetoCantina.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "compra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Long idCompra;

    @Column(name = "data_compra", nullable = false)
    private LocalDateTime dataCompra = LocalDateTime.now();

    @Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(length = 250)
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_funcionario")
    private Funcionario funcionario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_fornecedor")
    private Fornecedor fornecedor;

    public Compra() {
    }

    public Compra(Long idCompra, LocalDateTime dataCompra, BigDecimal valorTotal, String observacao, Funcionario funcionario, Fornecedor fornecedor) {
        this.idCompra = idCompra;
        this.dataCompra = dataCompra;
        this.valorTotal = valorTotal;
        this.observacao = observacao;
        this.funcionario = funcionario;
        this.fornecedor = fornecedor;
    }
    //=============================
    public Long getIdCompra() {
        return idCompra;
    }
    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }
    //=============================
    public LocalDateTime getDataCompra() {
        return dataCompra;
    }
    public void setDataCompra(LocalDateTime dataCompra) {
        this.dataCompra = dataCompra;
    }
    //=============================
    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    //=============================
    public String getObservacao() {
        return observacao;
    }
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    //=============================
    public Funcionario getFuncionario() {
        return funcionario;
    }
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
    //=============================
    public Fornecedor getFornecedor() {
        return fornecedor;
    }
    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
    //=============================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compra compra = (Compra) o;
        return Objects.equals(idCompra, compra.idCompra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCompra);
    }

    
}
