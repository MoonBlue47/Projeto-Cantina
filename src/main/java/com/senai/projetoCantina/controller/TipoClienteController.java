package com.senai.projetoCantina.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.senai.projetoCantina.model.TipoCliente;
import com.senai.projetoCantina.service.TipoClienteService;

@RestController
@RequestMapping("/api/tipos-cliente")
public class TipoClienteController {

    private final TipoClienteService tipoClienteService;

    public TipoClienteController(TipoClienteService tipoClienteService) {
        this.tipoClienteService = tipoClienteService;
    }

    @PostMapping
    public ResponseEntity<TipoCliente> cadastrar(@RequestBody TipoCliente tipoCliente) {
        TipoCliente novoTipo = tipoClienteService.cadastrar(tipoCliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoTipo);
    }

    @GetMapping
    public ResponseEntity<List<TipoCliente>> listarTodos() {
        return ResponseEntity.ok(tipoClienteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoCliente> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tipoClienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoCliente> atualizar(@PathVariable Long id, @RequestBody TipoCliente tipoCliente) {
        return ResponseEntity.ok(tipoClienteService.atualizar(id, tipoCliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        tipoClienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
