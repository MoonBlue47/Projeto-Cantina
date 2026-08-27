package com.senai.projetoCantina.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.senai.projetoCantina.dto.FormaPagamentoDto;
import com.senai.projetoCantina.service.FormaPagamentoService;

@Controller
@RequestMapping("/Forma-Pagamento")
public class FormaPagamentoController {
	private final FormaPagamentoService formaPagamentoService;
	public FormaPagamentoController(FormaPagamentoService formaPagamentoService) {
        this.formaPagamentoService = formaPagamentoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("formasPagamento", formaPagamentoService.findAll());
        return "formaPagamento/lista";
    }

    @GetMapping("/novo")
    public String exibirFormulario(Model model) {
        model.addAttribute("formaPagamento", new FormaPagamentoDto());
        return "formaPagamento/formulario";
    }

    @PostMapping("/novo")
    public String salvar(@ModelAttribute FormaPagamentoDto dto, RedirectAttributes flash) {
        try {
            formaPagamentoService.insert(dto);
            flash.addFlashAttribute("sucesso", "Forma de pagamento cadastrada com sucesso!");
            return "redirect:/Forma-Pagamento";
        } catch (Exception e) {
            flash.addFlashAttribute("erro", e.getMessage());
            return "redirect:/Forma-Pagamento/novo";
        }
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            formaPagamentoService.delete(id);
            flash.addFlashAttribute("sucesso", "Forma de pagamento removida com sucesso!");
        } catch (Exception e) {
            flash.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/Forma-Pagamento";
    }
}
