package com.senai.projetoCantina.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.senai.projetoCantina.model.Usuario;
import com.senai.projetoCantina.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * POST /api/auth/login
     * Body: { "login": "admin", "senha": "123456" }
     * Retorna 200 com { "sucesso": true, "perfil": "ADMIN" } ou 401
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String login = body.get("login");
        String senha = body.get("senha");

        if (login == null || senha == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Login e senha são obrigatórios"));
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByLogin(login);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("sucesso", false, "erro", "Usuário não encontrado"));
        }

        Usuario usuario = usuarioOpt.get();

        if (!Boolean.TRUE.equals(usuario.getAtivo())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("sucesso", false, "erro", "Usuário inativo"));
        }

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("sucesso", false, "erro", "Senha incorreta"));
        }

        String nomeExibicao = usuario.getFuncionario() != null
                ? usuario.getFuncionario().getNome()
                : usuario.getLogin();

        return ResponseEntity.ok(Map.of(
                "sucesso", true,
                "perfil", usuario.getPerfil().name(),
                "login", usuario.getLogin(),
                "nome", nomeExibicao
        ));
    }

    /**
     * POST /api/auth/cadastro
     * Body: { "login": "admin", "senha": "123456", "perfil": "ADMIN" }
     * Cria o primeiro usuário admin (sem exigir autenticação prévia).
     * O hash BCrypt é aplicado automaticamente na senha.
     */
    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastro(@RequestBody Map<String, String> body) {
        String login = body.get("login");
        String senha = body.get("senha");
        String perfilStr = body.getOrDefault("perfil", "OPERADOR");

        if (login == null || senha == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Login e senha são obrigatórios"));
        }

        if (usuarioRepository.findByLogin(login).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "Já existe um usuário com esse login"));
        }

        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setPerfil(Usuario.Perfil.valueOf(perfilStr.toUpperCase()));
        usuario.setAtivo(true);

        usuarioRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("sucesso", true, "mensagem", "Usuário criado com sucesso. Faça login."));
    }
}
