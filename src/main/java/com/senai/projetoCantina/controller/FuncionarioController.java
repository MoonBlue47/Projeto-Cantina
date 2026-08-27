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

import com.senai.projetoCantina.model.Funcionario;
import com.senai.projetoCantina.service.FuncionarioService;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {

	private final FuncionarioService funcionarioService;

	public FuncionarioController(FuncionarioService funcionarioService) {
		this.funcionarioService = funcionarioService;
	}

	@GetMapping
	public String listar(@RequestParam(required = false) String nome, Model model) {
		model.addAttribute("funcionarios", funcionarioService.buscarPorNome(nome));
		model.addAttribute("filtroNome", nome);
		return "funcionarios/lista";
	}

	@GetMapping("/novo")
	public String novoFormulario(Model model) {
		model.addAttribute("funcionario", new Funcionario());
		return "funcionarios/formulario";
	}

	@GetMapping("/editar/{id}")
	public String editarFormulario(@PathVariable Long id, Model model) {
		model.addAttribute("funcionario", funcionarioService.buscarPorId(id));
		return "funcionarios/formulario";
	}

	@PostMapping("/salvar")
	public String salvar(@ModelAttribute Funcionario funcionario, RedirectAttributes flash) {
		try {
			if (funcionario.getId() == null) {
				funcionarioService.cadastrar(funcionario);
				flash.addFlashAttribute("sucesso", "Funcionário cadastrado com sucesso!");
			} else {
				funcionarioService.atualizar(funcionario.getId(), funcionario);
				flash.addFlashAttribute("sucesso", "Funcionário atualizado com sucesso!");
			}
			return "redirect:/funcionarios";
		} catch (IllegalStateException e) {
			flash.addFlashAttribute("erro", e.getMessage());
			return "redirect:/funcionarios/novo";
		}
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable Long id) {
		funcionarioService.excluir(id);
		return "redirect:/funcionarios";
	}
}
