package com.senai.projetoCantina.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senai.projetoCantina.model.Usuario;
import com.senai.projetoCantina.repository.UsuarioRepository;

import com.senai.projetoCantina.exception.*;
import com.senai.projetoCantina.exception.RecursoNaoEncontradoException;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario cadastrar(Usuario usuario) {
        if (usuarioRepository.findByLogin(usuario.getLogin()).isPresent()) {
            throw new IllegalStateException("Já existe um usuário cadastrado com esse login");
        }
        if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
    }

    @Transactional
    public Usuario atualizar(Long id, Usuario dadosNovos) {
        Usuario existente = buscarPorId(id);

        Optional<Usuario> usuarioComMesmoLogin = usuarioRepository.findByLogin(dadosNovos.getLogin());
        if (usuarioComMesmoLogin.isPresent() && !usuarioComMesmoLogin.get().getId().equals(id)) {
            throw new IllegalStateException("Já existe outro usuário cadastrado com esse login");
        }

        existente.setLogin(dadosNovos.getLogin());
        if (dadosNovos.getSenha() != null && !dadosNovos.getSenha().isBlank()) {
            existente.setSenha(passwordEncoder.encode(dadosNovos.getSenha()));
        }
        existente.setPerfil(dadosNovos.getPerfil());
        existente.setAtivo(dadosNovos.getAtivo());
        existente.setFuncionario(dadosNovos.getFuncionario());

        return usuarioRepository.save(existente);
    }

    @Transactional
    public void excluir(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }
}