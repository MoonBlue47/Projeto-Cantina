package com.senai.projetoCantina.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.senai.projetoCantina.model.Cliente;
import com.senai.projetoCantina.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@RequestBody Cliente cliente) {
        Cliente novoCliente = clienteService.cadastrar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.atualizar(id, cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        clienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/clientes/matricula/{matricula}
     * Usado pelo totem para validar se a matrícula existe no banco.
     */
    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<?> buscarPorMatricula(@PathVariable String matricula) {
        Optional<com.senai.projetoCantina.model.Cliente> cliente =
                clienteService.buscarPorMatricula(matricula);
        if (cliente.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(Map.of("encontrado", false, "erro", "Matrícula não cadastrada"));
        }
        return ResponseEntity.ok(Map.of(
                "encontrado", true,
                "id",         cliente.get().getId(),
                "nome",       cliente.get().getNome(),
                "matricula",  cliente.get().getMatricula()
        ));
    }
}
