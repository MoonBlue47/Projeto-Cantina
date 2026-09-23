package com.senai.projetoCantina.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.senai.projetoCantina.model.Estoque;
import com.senai.projetoCantina.model.Produto;
import com.senai.projetoCantina.repository.EstoqueRepository;
import com.senai.projetoCantina.repository.ProdutoRepository;
import com.senai.projetoCantina.service.EstoqueService;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueRestController {

    private final EstoqueService estoqueService;
    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;

    public EstoqueRestController(EstoqueService estoqueService,
                                  EstoqueRepository estoqueRepository,
                                  ProdutoRepository produtoRepository) {
        this.estoqueService = estoqueService;
        this.estoqueRepository = estoqueRepository;
        this.produtoRepository = produtoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Estoque>> listarEstoque() {
        return ResponseEntity.ok(estoqueRepository.findAll());
    }

    @PostMapping("/entrada")
    public ResponseEntity<?> registrarEntrada(@RequestBody Map<String, Object> body) {
        Number idProduto    = (Number) body.get("idProduto");
        Number quantidade   = (Number) body.get("quantidade");
        String origem       = (String) body.getOrDefault("origem", "Manual");
        Number valorUnit    = (Number) body.getOrDefault("valorUnitario", 0);

        Produto produto = produtoRepository.findById(idProduto.longValue())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        try {
            return ResponseEntity.ok(
                    estoqueService.registrarEntrada(produto, quantidade.intValue(), origem, valorUnit.doubleValue())
            );
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping("/saida")
    public ResponseEntity<?> registrarSaida(@RequestBody Map<String, Object> body) {
        Number idProduto  = (Number) body.get("idProduto");
        Number quantidade = (Number) body.get("quantidade");
        String origem     = (String) body.getOrDefault("origem", "Manual");

        Produto produto = produtoRepository.findById(idProduto.longValue())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        try {
            return ResponseEntity.ok(
                    estoqueService.registrarSaida(produto, quantidade.intValue(), origem)
            );
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
}
