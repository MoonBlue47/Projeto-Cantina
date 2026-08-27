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

import com.senai.projetoCantina.dto.VendaRequestDto;
import com.senai.projetoCantina.service.FormaPagamentoService;
import com.senai.projetoCantina.service.VendaService;

@Controller
@RequestMapping("/vendas")
public class VendaController {
	private final VendaService vendaService;
    private final FormaPagamentoService formaPagamentoService;

    public VendaController(VendaService vendaService, FormaPagamentoService formaPagamentoService) {
        this.vendaService = vendaService;
        this.formaPagamentoService = formaPagamentoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("vendas", vendaService.findAll());
        return "venda/lista";
    }

    @GetMapping("/novo")
    public String exibirFormulario(Model model) {
        model.addAttribute("venda", new VendaRequestDto());
        model.addAttribute("formasPagamento", formaPagamentoService.findAll());
        return "venda/formulario";
    }

    @PostMapping("/novo")
    public String salvar(@ModelAttribute VendaRequestDto dto, RedirectAttributes flash) {
        try {
            vendaService.registrarVenda(dto);
            flash.addFlashAttribute("sucesso", "Venda registrada com sucesso!");
            return "redirect:/vendas";
        } catch (Exception e) {
            flash.addFlashAttribute("erro", e.getMessage());
            return "redirect:/vendas/novo";
        }
    }

    @PostMapping("/{id}/status")
    public String atualizarStatus(@PathVariable Long id, @RequestParam String status, RedirectAttributes flash) {
        try {
            vendaService.atualizarStatus(id, status);
            flash.addFlashAttribute("sucesso", "Status da venda atualizado com sucesso!");
        } catch (Exception e) {
            flash.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/vendas";
    }

}
