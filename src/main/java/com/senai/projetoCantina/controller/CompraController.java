package com.senai.projetoCantina.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.senai.projetoCantina.model.Compra;
import com.senai.projetoCantina.service.CompraService;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping
    public ResponseEntity<Compra> cadastrar(@RequestBody Compra compra) {
        Compra novaCompra = compraService.cadastrar(compra);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCompra);
    }

    @GetMapping
    public ResponseEntity<List<Compra>> listarTodas() {
        return ResponseEntity.ok(compraService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compra> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(compraService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Compra> atualizar(@PathVariable Long id, @RequestBody Compra compra) {
        return ResponseEntity.ok(compraService.atualizar(id, compra));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        compraService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}