package com.senai.projetoCantina.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.senai.projetoCantina.model.Usuario;
import com.senai.projetoCantina.repository.FuncionarioRepository;
import com.senai.projetoCantina.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository,
                          FuncionarioRepository funcionarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

        boolean senhaValida = false;
        try {
            senhaValida = passwordEncoder.matches(senha, usuario.getSenha());
        } catch (Exception ignored) {}

        if (!senhaValida && senha.equals(usuario.getSenha())) {
            senhaValida = true;
            usuario.setSenha(passwordEncoder.encode(senha));
            usuarioRepository.save(usuario);
        }

        if (!senhaValida) {
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

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastro(@RequestBody Map<String, String> body) {
        String login = body.get("login");
        String senha = body.get("senha");
        String perfilStr = body.getOrDefault("perfil", "OPERADOR");
        String idFuncionarioStr = body.get("idFuncionario");

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

        // Vincular ao funcionário, se informado
        if (idFuncionarioStr != null && !idFuncionarioStr.isBlank()) {
            try {
                Long idFuncionario = Long.parseLong(idFuncionarioStr);
                com.senai.projetoCantina.model.Funcionario funcionario =
                        funcionarioRepository.findById(idFuncionario).orElse(null);
                if (funcionario != null) {
                    usuario.setFuncionario(funcionario);
                }
            } catch (NumberFormatException ignored) {}
        }

        usuarioRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("sucesso", true, "mensagem", "Usuário criado com sucesso."));
    }
}
