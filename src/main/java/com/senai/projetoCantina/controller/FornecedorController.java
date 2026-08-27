package com.senai.projetoCantina.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.senai.projetoCantina.model.Fornecedor;
import com.senai.projetoCantina.service.FornecedorService;

@Controller
@RequestMapping("/fornecedores")
public class FornecedorController {

	private final FornecedorService fornecedorService;

	public FornecedorController(FornecedorService fornecedorService) {
		this.fornecedorService = fornecedorService;
	}

	@GetMapping
	public String listar(@RequestParam(required = false) String nome, Model model) {
		model.addAttribute("fornecedores", fornecedorService.buscarPorNome(nome));
		model.addAttribute("filtroNome", nome);
		return "fornecedores/lista";
	}

	@GetMapping("/novo")
	public String novoFormulario(Model model) {
		model.addAttribute("fornecedor", new Fornecedor());
		return "fornecedores/formulario";
	}

	@GetMapping("/editar/{id}")
	public String editarFormulario(@PathVariable Long id, Model model) {
		model.addAttribute("fornecedor", fornecedorService.buscarPorId(id));
		return "fornecedores/formulario";
	}

	@PostMapping("/salvar")
	public String salvar(@ModelAttribute Fornecedor fornecedor, RedirectAttributes flash) {
		try {
			if (fornecedor.getId() == null) {
				fornecedorService.cadastrar(fornecedor);
				flash.addFlashAttribute("sucesso", "Fornecedor cadastrado com sucesso!");
			} else {
				fornecedorService.atualizar(fornecedor.getId(), fornecedor);
				flash.addFlashAttribute("sucesso", "Fornecedor atualizado com sucesso!");
			}
			return "redirect:/fornecedores";
		} catch (IllegalStateException e) {
			flash.addFlashAttribute("erro", e.getMessage());
			return "redirect:/fornecedores/novo";
		}
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable Long id) {
		fornecedorService.excluir(id);
		return "redirect:/fornecedores";
	}
}
