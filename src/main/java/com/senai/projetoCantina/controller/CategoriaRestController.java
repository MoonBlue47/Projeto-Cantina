package com.senai.projetoCantina.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.senai.projetoCantina.model.Categoria;
import com.senai.projetoCantina.repository.CategoriaRepository;
import com.senai.projetoCantina.service.CategoriaService;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaRestController {

    private final CategoriaService categoriaService;
    private final CategoriaRepository categoriaRepository;

    public CategoriaRestController(CategoriaService categoriaService,
                                   CategoriaRepository categoriaRepository) {
        this.categoriaService = categoriaService;
        this.categoriaRepository = categoriaRepository;
    }

    /** GET /api/categorias — lista todas para popular selects no front */
    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodas() {
        return ResponseEntity.ok(categoriaRepository.findAll());
    }

    /** POST /api/categorias — cria nova categoria */
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Categoria categoria) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.cadastrar(categoria));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /** GET /api/categorias/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Long id) {
        return categoriaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** PUT /api/categorias/{id} — atualiza categoria existente */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Categoria dadosNovos) {
        return categoriaRepository.findById(id)
                .map(existente -> {
                    existente.setNome(dadosNovos.getNome());
                    existente.setDescricao(dadosNovos.getDescricao());
                    return ResponseEntity.ok(categoriaRepository.save(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /** DELETE /api/categorias/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        categoriaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
