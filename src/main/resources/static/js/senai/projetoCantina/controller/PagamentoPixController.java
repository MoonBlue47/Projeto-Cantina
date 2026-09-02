package com.senai.projetoCantina.controller;

import com.senai.projetoCantina.exception.RecursoNaoEncontradoException;
import com.senai.projetoCantina.model.Venda;
import com.senai.projetoCantina.model.Venda.StatusVenda;
import com.senai.projetoCantina.repository.VendaRepository;
import com.senai.projetoCantina.service.PixService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller MVC responsavel pela pagina de pagamento Pix.
 *
 * Rotas:
 *   GET  /pagamento/pix/{pedidoId}                  -> exibe a pagina com QR Code
 *   POST /pagamento/pix/{pedidoId}/confirmar-demo   -> simula aprovacao e redireciona
 */
@Controller
@RequestMapping("/pagamento/pix")
public class PagamentoPixController {

    private final VendaRepository vendaRepository;
    private final PixService pixService;

    public PagamentoPixController(VendaRepository vendaRepository, PixService pixService) {
        this.vendaRepository = vendaRepository;
        this.pixService = pixService;
    }

    /**
     * Exibe a pagina de pagamento Pix com o QR Code gerado.
     */
    @GetMapping("/{pedidoId}")
    public String exibirPaginaPix(@PathVariable Long pedidoId, Model model) {
        Venda venda = vendaRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Venda", pedidoId));

        String pixCopiaCola = pixService.gerarPixCopiaCola(venda.getValorTotal());
        String qrCodeBase64 = pixService.gerarQrCodeBase64(pixCopiaCola);

        model.addAttribute("pedidoId",     venda.getId());
        model.addAttribute("valorTotal",   venda.getValorTotal());
        model.addAttribute("status",       venda.getStatus().name());
        model.addAttribute("pixCopiaCola", pixCopiaCola);
        model.addAttribute("qrCodeBase64", qrCodeBase64);

        return "pagamento-pix";
    }

    /**
     * Simula a confirmacao de pagamento Pix (modo apresentacao).
     * Altera o status da venda para CONCLUIDO e redireciona com mensagem de sucesso.
     */
    @PostMapping("/{pedidoId}/confirmar-demo")
    public String confirmarPagamentoDemo(@PathVariable Long pedidoId,
                                         RedirectAttributes redirectAttributes) {
        Venda venda = vendaRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Venda", pedidoId));

        venda.setStatus(StatusVenda.CONCLUIDO);
        vendaRepository.save(venda);

        redirectAttributes.addFlashAttribute("pagamentoConfirmado", true);
        redirectAttributes.addFlashAttribute("mensagem",
                "Pagamento Pix confirmado! Venda #" + pedidoId + " concluida.");

        return "redirect:/pagamento/pix/" + pedidoId;
    }
}