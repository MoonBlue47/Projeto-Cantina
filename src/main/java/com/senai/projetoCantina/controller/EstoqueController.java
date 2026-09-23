package com.senai.projetoCantina.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.senai.projetoCantina.model.Produto;
import com.senai.projetoCantina.repository.EstoqueRepository;
import com.senai.projetoCantina.repository.ProdutoRepository;
import com.senai.projetoCantina.service.EstoqueService;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {
	
	private final EstoqueService estoqueService;
	private final EstoqueRepository estoqueRepository;
	private final ProdutoRepository produtoRepository;
	
	public EstoqueController(EstoqueService estoqueService, EstoqueRepository estoqueRepository, ProdutoRepository produtoRepository) {
		this.estoqueService = estoqueService;
		this.estoqueRepository = estoqueRepository;
		this.produtoRepository = produtoRepository;
	}
	
	@GetMapping
	public String listar(Model model) {
		model.addAttribute("listarEstoque", estoqueRepository.findAll());
		return "estoque/listar";
	}
	
	@GetMapping("/entrada")
	public String exibirFormularioEntrada(Model model) {
		model.addAttribute("produtos", produtoRepository.findAll());
		return "estoque/entrada";
	}
	
	@PostMapping("/entrada")
	public String registrarEntrada(@RequestParam Produto produto, @RequestParam int quantidade, @RequestParam String origem, @RequestParam double valorUnitario, RedirectAttributes flash) {
		try {
			estoqueService.registrarEntrada(produto, quantidade, origem, valorUnitario);
			flash.addFlashAttribute("sucesso", "Entrada de estoque registrada com sucesso");
			return "redirect:/estoque";
		} catch (IllegalStateException e) {
			flash.addFlashAttribute("erro", e.getMessage());
			return "redirect:/estoque/entrada";
		}
	}
	
	@GetMapping("/saida")
	public String exibirFormularioSaida(Model model) {
		model.addAttribute("produtos", produtoRepository.findAll());
		return "estoque/saida";
	}
	
	@PostMapping("/saida")
	public String registrarSaida(@RequestParam Produto produto, @RequestParam int quantidade, @RequestParam String origem,  RedirectAttributes flash) {
		try {
			estoqueService.registrarSaida(produto, quantidade, origem);
			flash.addFlashAttribute("sucesso", "Saida do estoque registrada com sucesso");
			return "redirect:/estoque";
		} catch (IllegalStateException e) {
			flash.addFlashAttribute("erro", e.getMessage());
			return "redirect:/estoque/saida";
		}
	}
}
