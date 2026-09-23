package com.senai.projetoCantina.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "venda")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venda")
    private Long id;

    @Column(name = "data_venda", nullable = false)
    private LocalDateTime dataVenda = LocalDateTime.now();

    @Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Convert(converter = StatusVendaConverter.class)
    @Column(name = "status", nullable = false, length = 50)
    private StatusVenda status = StatusVenda.EM_ANDAMENTO;

    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "id_funcionario")
    private Long idFuncionario;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itens = new ArrayList<>();

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendaPagamento> pagamentos = new ArrayList<>();

    public Venda() {
    }

    public Venda(Long id, LocalDateTime dataVenda, BigDecimal valorTotal, StatusVenda status, Long idCliente, Long idFuncionario) {
        this.id = id;
        this.dataVenda = dataVenda;
        this.valorTotal = valorTotal;
        this.status = status != null ? status : StatusVenda.EM_ANDAMENTO;
        this.idCliente = idCliente;
        this.idFuncionario = idFuncionario;
    }

    public void adicionarItem(ItemVenda item) {
        itens.add(item);
        item.setVenda(this);
    }

    public void removerItem(ItemVenda item) {
        itens.remove(item);
        item.setVenda(null);
    }

    public void adicionarPagamento(VendaPagamento pagamento) {
        pagamentos.add(pagamento);
        pagamento.setVenda(this);
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

	public StatusVenda getStatus() {
		return status;
	}

	public void setStatus(StatusVenda status) {
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

	public List<ItemVenda> getItens() {
		return itens;
	}

	public void setItens(List<ItemVenda> itens) {
		this.itens = itens;
	}

	public List<VendaPagamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<VendaPagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venda venda = (Venda) o;
        return Objects.equals(id, venda.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    public enum StatusVenda {
        EM_ANDAMENTO("em andamento"),
        CONCLUIDO("concluído"),
        CANCELADO("cancelado");

        private final String valorBanco;

        StatusVenda(String valorBanco) {
            this.valorBanco = valorBanco;
        }

        public String getValorBanco() {
            return valorBanco;
        }
    }
}