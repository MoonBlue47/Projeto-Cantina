package com.senai.projetoCantina.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.senai.projetoCantina.dto.VendaRequestDto;
import com.senai.projetoCantina.dto.VendaResponseDto;
import com.senai.projetoCantina.service.VendaService;

@RestController
@RequestMapping("/api/vendas")
public class VendaRestController {

    private final VendaService vendaService;

    public VendaRestController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @GetMapping
    public ResponseEntity<List<VendaResponseDto>> listarTodas() {
        return ResponseEntity.ok(vendaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vendaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<VendaResponseDto> registrar(@RequestBody VendaRequestDto dto) {
        VendaResponseDto venda = vendaService.registrarVenda(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(venda);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<VendaResponseDto> atualizarStatus(@PathVariable Long id,
                                                             @RequestParam String status) {
        return ResponseEntity.ok(vendaService.atualizarStatus(id, status));
    }
}
