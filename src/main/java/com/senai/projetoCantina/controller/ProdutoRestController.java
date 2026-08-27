package com.senai.projetoCantina.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.senai.projetoCantina.model.Categoria;
import com.senai.projetoCantina.model.Estoque;
import com.senai.projetoCantina.model.Produto;
import com.senai.projetoCantina.repository.CategoriaRepository;
import com.senai.projetoCantina.repository.EstoqueRepository;
import com.senai.projetoCantina.repository.ProdutoRepository;
import com.senai.projetoCantina.service.ProdutoService;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoRestController {

    private final ProdutoService produtoService;
    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final EstoqueRepository estoqueRepository;

    public ProdutoRestController(ProdutoService produtoService,
                                 ProdutoRepository produtoRepository,
                                 CategoriaRepository categoriaRepository,
                                 EstoqueRepository estoqueRepository) {
        this.produtoService = produtoService;
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.estoqueRepository = estoqueRepository;
    }

    /**
     * GET /api/produtos
     * Retorna lista de produtos com id, nome, preco, categoria e estoque atual.
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarTodos() {
        List<Map<String, Object>> result = produtoRepository.findAll().stream()
                .map(p -> {
                    int estoqueQtd = estoqueRepository.findByProdutoId(p.getId())
                            .map(Estoque::getQuantidade)
                            .orElse(0);

                    Map<String, Object> map = new HashMap<>();
                    map.put("id",          p.getId());
                    map.put("nome",        p.getNome());
                    map.put("preco",       p.getPrecoVendas());
                    map.put("descricao",   p.getCategoria() != null ? p.getCategoria().getDescricao() : "");
                    map.put("categoria",   p.getCategoria() != null ? p.getCategoria().getNome() : "Sem categoria");
                    map.put("idCategoria", p.getCategoria() != null ? p.getCategoria().getId() : null);
                    map.put("estoque",     estoqueQtd);
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/produtos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .map(p -> {
                    int estoqueQtd = estoqueRepository.findByProdutoId(p.getId())
                            .map(Estoque::getQuantidade)
                            .orElse(0);

                    Map<String, Object> map = new HashMap<>();
                    map.put("id",          p.getId());
                    map.put("nome",        p.getNome());
                    map.put("preco",       p.getPrecoVendas());
                    map.put("descricao",   p.getCategoria() != null ? p.getCategoria().getDescricao() : "");
                    map.put("categoria",   p.getCategoria() != null ? p.getCategoria().getNome() : "Sem categoria");
                    map.put("idCategoria", p.getCategoria() != null ? p.getCategoria().getId() : null);
                    map.put("estoque",     estoqueQtd);
                    return ResponseEntity.ok(map);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/produtos
     * Body: { "nome": "Coxinha", "idCategoria": 1, "preco": 6.50 }
     */
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Map<String, Object> body) {
        String nome = (String) body.get("nome");
        Number preco = (Number) body.get("preco");
        Number idCategoria = (Number) body.get("idCategoria");

        if (nome == null || preco == null || idCategoria == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Campos nome, preco e idCategoria são obrigatórios"));
        }

        Categoria categoria = categoriaRepository.findById(idCategoria.longValue())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada. ID: " + idCategoria));

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setPrecoVendas(preco.doubleValue());
        produto.setCategoria(categoria);

        try {
            Produto salvo = produtoService.cadastrar(produto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "id",        salvo.getId(),
                    "nome",      salvo.getNome(),
                    "preco",     salvo.getPrecoVendas(),
                    "categoria", salvo.getCategoria().getNome()
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", e.getMessage()));
        }
    }

    /**
     * PUT /api/produtos/{id}
     * Body: { "nome": "Coxinha Assada", "idCategoria": 1, "preco": 7.00 }
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String nome = (String) body.get("nome");
        Number preco = (Number) body.get("preco");
        Number idCategoria = (Number) body.get("idCategoria");

        return produtoRepository.findById(id)
                .map(existente -> {
                    if (nome != null && !nome.isBlank()) existente.setNome(nome);
                    if (preco != null) existente.setPrecoVendas(preco.doubleValue());
                    if (idCategoria != null) {
                        Categoria cat = categoriaRepository.findById(idCategoria.longValue())
                                .orElseThrow(() -> new RuntimeException("Categoria não encontrada. ID: " + idCategoria));
                        existente.setCategoria(cat);
                    }
                    Produto salvo = produtoRepository.save(existente);
                    return ResponseEntity.ok(Map.of(
                            "id",        salvo.getId(),
                            "nome",      salvo.getNome(),
                            "preco",     salvo.getPrecoVendas(),
                            "categoria", salvo.getCategoria() != null ? salvo.getCategoria().getNome() : ""
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/produtos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
