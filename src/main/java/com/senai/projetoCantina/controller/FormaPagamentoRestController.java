package com.senai.projetoCantina.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.senai.projetoCantina.dto.FormaPagamentoDto;
import com.senai.projetoCantina.service.FormaPagamentoService;

@RestController
@RequestMapping("/api/formas-pagamento")
public class FormaPagamentoRestController {

    private final FormaPagamentoService formaPagamentoService;

    public FormaPagamentoRestController(FormaPagamentoService formaPagamentoService) {
        this.formaPagamentoService = formaPagamentoService;
    }

    /** GET /api/formas-pagamento */
    @GetMapping
    public ResponseEntity<List<FormaPagamentoDto>> listarTodas() {
        return ResponseEntity.ok(formaPagamentoService.findAll());
    }

    /** GET /api/formas-pagamento/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<FormaPagamentoDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(formaPagamentoService.findById(id));
    }

    /** POST /api/formas-pagamento — Body: { "tipo": "PIX" } */
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody FormaPagamentoDto dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(formaPagamentoService.insert(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** DELETE /api/formas-pagamento/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        formaPagamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
