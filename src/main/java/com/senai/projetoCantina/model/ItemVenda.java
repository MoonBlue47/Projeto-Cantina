package com.senai.projetoCantina.model;

	import jakarta.persistence.*;
	import java.math.BigDecimal;
	import java.util.Objects;

	@Entity
	@Table(name = "item_venda")
	public class ItemVenda {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id_item_venda")
	    private Long id;

	    @Column(name = "quantidade", nullable = false)
	    private Integer quantidade;

	    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
	    private BigDecimal precoUnitario;

	    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
	    private BigDecimal subtotal;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "id_venda")
	    private Venda venda;

	    @Column(name = "id_produto")
	    private Long idProduto;

	    public ItemVenda() {
	    }

	    public ItemVenda(Long id, Integer quantidade, BigDecimal precoUnitario, Venda venda, Long idProduto) {
	        this.id = id;
	        this.quantidade = quantidade;
	        this.precoUnitario = precoUnitario;
	        this.venda = venda;
	        this.idProduto = idProduto;
	        calcularSubtotal();
	    }

	    public void calcularSubtotal() {
	        if (this.precoUnitario != null && this.quantidade != null) {
	            this.subtotal = this.precoUnitario.multiply(BigDecimal.valueOf(this.quantidade));
	        } else {
	            this.subtotal = BigDecimal.ZERO;
	        }
	    }

	   
	    public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Integer getQuantidade() {
			return quantidade;
		}

		public void setQuantidade(Integer quantidade) {
			this.quantidade = quantidade;
		}

		public BigDecimal getPrecoUnitario() {
			return precoUnitario;
		}

		public void setPrecoUnitario(BigDecimal precoUnitario) {
			this.precoUnitario = precoUnitario;
		}

		public BigDecimal getSubtotal() {
			return subtotal;
		}

		public void setSubtotal(BigDecimal subtotal) {
			this.subtotal = subtotal;
		}

		public Venda getVenda() {
			return venda;
		}

		public void setVenda(Venda venda) {
			this.venda = venda;
		}

		public Long getIdProduto() {
			return idProduto;
		}

		public void setIdProduto(Long idProduto) {
			this.idProduto = idProduto;
		}

		@Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        ItemVenda itemVenda = (ItemVenda) o;
	        return Objects.equals(id, itemVenda.id);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(id);
	    }
	
}
