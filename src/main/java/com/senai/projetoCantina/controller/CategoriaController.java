package com.senai.projetoCantina.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.senai.projetoCantina.model.Categoria;
import com.senai.projetoCantina.repository.CategoriaRepository;
import com.senai.projetoCantina.service.CategoriaService;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {
	
	private final CategoriaService categoriaService;
	private final CategoriaRepository categoriaRepository;
	
	public CategoriaController(CategoriaService categoriaService, CategoriaRepository categoriaRepository) {
		this.categoriaService = categoriaService;
		this.categoriaRepository = categoriaRepository;
	}
	
	@GetMapping
	public String listar(Model model) {
		model.addAttribute("categorias", categoriaRepository.findAll());
		return "categoria/lista";
	}
	
	@GetMapping("/novo")
	public String exibirFormulario(Model model) {
		model.addAttribute("categoria", new Categoria());
		return "categoria/formulario";
	}
	
	@PostMapping("/novo")
	public String salvar(@ModelAttribute Categoria categoria, RedirectAttributes flash) {
		try {
			categoriaService.cadastrar(categoria);
			flash.addFlashAttribute("sucesso", "Categoria cadastrada com sucesso!");
			return "redirect:/categorias";
		} catch (IllegalStateException e) {
			flash.addFlashAttribute("erro", e.getMessage());
			return "redirect:/categorias/novo";
		}
	}

}
