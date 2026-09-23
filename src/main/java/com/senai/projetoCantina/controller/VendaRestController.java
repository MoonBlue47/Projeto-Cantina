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

    /**
     * GET /api/vendas
     * Lista todas as vendas com itens e pagamentos — usado pelo painel Admin.
     */
    @GetMapping
    public ResponseEntity<List<VendaResponseDto>> listarTodas() {
        return ResponseEntity.ok(vendaService.findAll());
    }

    /**
     * GET /api/vendas/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vendaService.findById(id));
    }

    /**
     * POST /api/vendas
     * Body: {
     *   "idCliente": 1,
     *   "idFuncionario": null,
     *   "itens": [{ "idProduto": 1, "quantidade": 2, "precoUnitario": 6.50 }],
     *   "pagamentos": [{ "idFormaPagamento": 1, "valor": 13.00 }]
     * }
     */
    @PostMapping
    public ResponseEntity<VendaResponseDto> registrar(@RequestBody VendaRequestDto dto) {
        VendaResponseDto venda = vendaService.registrarVenda(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(venda);
    }

    /**
     * PUT /api/vendas/{id}/status
     * Body: { "status": "CONCLUIDO" }
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<VendaResponseDto> atualizarStatus(@PathVariable Long id,
                                                             @RequestParam String status) {
        return ResponseEntity.ok(vendaService.atualizarStatus(id, status));
    }
}
