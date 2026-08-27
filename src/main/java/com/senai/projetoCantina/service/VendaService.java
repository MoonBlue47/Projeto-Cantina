package com.senai.projetoCantina.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.senai.projetoCantina.dto.*;
import com.senai.projetoCantina.model.*;
import com.senai.projetoCantina.repository.*;

@Service
public class VendaService {
    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EstoqueService estoqueService;

    @Transactional(readOnly = true)
    public List<VendaResponseDto> findAll() {
        return vendaRepository.findAll().stream()
                .map(VendaResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VendaResponseDto findById(Long id) {
        Venda entity = vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada. ID: " + id));
        return new VendaResponseDto(entity);
    }

    @Transactional
    public VendaResponseDto registrarVenda(VendaRequestDto dto) {
        Venda venda = new Venda();
        venda.setDataVenda(LocalDateTime.now());
        venda.setStatus(Venda.StatusVenda.EM_ANDAMENTO);
        venda.setIdCliente(dto.getIdCliente());
        venda.setIdFuncionario(dto.getIdFuncionario());

        BigDecimal totalVenda = BigDecimal.ZERO;

        if (dto.getItens() != null) {
            for (ItemVendaDto itemDto : dto.getItens()) {
                ItemVenda item = new ItemVenda();
                item.setIdProduto(itemDto.getIdProduto());
                item.setQuantidade(itemDto.getQuantidade());
                item.setPrecoUnitario(itemDto.getPrecoUnitario());
                item.calcularSubtotal();
                
                totalVenda = totalVenda.add(item.getSubtotal());
                venda.adicionarItem(item);
            }
        }

        venda.setValorTotal(totalVenda);

        if (dto.getPagamentos() != null) {
            for (VendaPagamentoDto pagDto : dto.getPagamentos()) {
                FormaPagamento forma = formaPagamentoRepository.findById(pagDto.getIdFormaPagamento())
                        .orElseThrow(() -> new RuntimeException("Forma de pagamento inválida. ID: " + pagDto.getIdFormaPagamento()));
                
                VendaPagamento pagamento = new VendaPagamento();
                pagamento.setFormaPagamento(forma);
                pagamento.setValorPago(pagDto.getValor());
                
                venda.adicionarPagamento(pagamento);
            }
        }

        venda = vendaRepository.save(venda);

        if (dto.getItens() != null) {
            for (ItemVendaDto itemDto : dto.getItens()) {
                Produto produto = produtoRepository.findById(itemDto.getIdProduto())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado. ID: " + itemDto.getIdProduto()));
                estoqueService.registrarSaida(produto, itemDto.getQuantidade(), "Venda #" + venda.getId());
            }
        }

        return new VendaResponseDto(venda);
    }

    @Transactional
    public VendaResponseDto atualizarStatus(Long id, String novoStatus) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada. ID: " + id));

        venda.setStatus(Venda.StatusVenda.valueOf(novoStatus.toUpperCase()));
        venda = vendaRepository.save(venda);
        return new VendaResponseDto(venda);
    }
}
