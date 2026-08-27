package com.senai.projetoCantina.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.senai.projetoCantina.model.Produto;
import com.senai.projetoCantina.repository.ProdutoRepository;
import com.senai.projetoCantina.service.ProdutoService;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {
	
	private final ProdutoService produtoService;
	private final ProdutoRepository produtoRepository;
	
	public ProdutoController(ProdutoService produtoService, ProdutoRepository produtoRepository) {
		this.produtoService = produtoService;
		this.produtoRepository = produtoRepository;
	}
	
	@GetMapping
	public String listar(Model model) {
		model.addAttribute("produtos", produtoRepository.findAll());
		return "produto/listar";
	}
	
	@GetMapping("/novo")
	public String exibirFormulario(Model model) {
		model.addAttribute("produto", new Produto());
		return "produto/formulario";
	}
	
	@PostMapping("/novo")
	public String salvar(@ModelAttribute Produto produto, RedirectAttributes flash) {
		try {
			produtoService.cadastrar(produto);
			flash.addFlashAttribute("sucesso", "Produto cadastrado com sucesso!");
			return "redirect:/produtos";
		} catch (IllegalStateException e) {
			flash.addFlashAttribute("erro", e.getMessage());
			return "redirect:/produtos/novo";
		}
	}
}
