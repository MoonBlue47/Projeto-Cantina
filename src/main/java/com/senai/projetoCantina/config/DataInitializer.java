package com.senai.projetoCantina.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.senai.projetoCantina.model.*;
import com.senai.projetoCantina.repository.*;
import com.senai.projetoCantina.service.EstoqueService;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final TipoClienteRepository tipoClienteRepository;
    private final ClienteRepository clienteRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final EstoqueRepository estoqueRepository;
    private final EstoqueService estoqueService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           FuncionarioRepository funcionarioRepository,
                           TipoClienteRepository tipoClienteRepository,
                           ClienteRepository clienteRepository,
                           CategoriaRepository categoriaRepository,
                           ProdutoRepository produtoRepository,
                           FormaPagamentoRepository formaPagamentoRepository,
                           EstoqueRepository estoqueRepository,
                           EstoqueService estoqueService,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.tipoClienteRepository = tipoClienteRepository;
        this.clienteRepository = clienteRepository;
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
        this.formaPagamentoRepository = formaPagamentoRepository;
        this.estoqueRepository = estoqueRepository;
        this.estoqueService = estoqueService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        TipoCliente tipoAluno = tipoClienteRepository.findByNome("Aluno").orElseGet(() -> {
            TipoCliente tc = new TipoCliente();
            tc.setNome("Aluno");
            return tipoClienteRepository.save(tc);
        });

        if (clienteRepository.findByMatricula("1001").isEmpty()) {
            Cliente cliente = new Cliente();
            cliente.setNome("Aluno Teste");
            cliente.setMatricula("1001");
            cliente.setIdTipoCliente(tipoAluno.getId());
            clienteRepository.save(cliente);
        }

        Funcionario funcionario = funcionarioRepository.findAll().stream().findFirst().orElseGet(() -> {
            Funcionario f = new Funcionario();
            f.setNome("Administrador Sistema");
            f.setCpf("000.000.000-00");
            f.setCargo("Gerente");
            f.setAtivo(true);
            return funcionarioRepository.save(f);
        });

        if (usuarioRepository.findByLogin("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setLogin("admin");
            admin.setSenha(passwordEncoder.encode("123456"));
            admin.setPerfil(Usuario.Perfil.ADMIN);
            admin.setAtivo(true);
            admin.setFuncionario(funcionario);
            usuarioRepository.save(admin);
        }

        if (formaPagamentoRepository.findAll().isEmpty()) {
            FormaPagamento pix = new FormaPagamento();
            pix.setTipo(FormaPagamento.TipoPagamento.PIX);
            formaPagamentoRepository.save(pix);

            FormaPagamento din = new FormaPagamento();
            din.setTipo(FormaPagamento.TipoPagamento.DINHEIRO);
            formaPagamentoRepository.save(din);

            FormaPagamento cartao = new FormaPagamento();
            cartao.setTipo(FormaPagamento.TipoPagamento.CARTAO_DEBITO);
            formaPagamentoRepository.save(cartao);
        }

        if (categoriaRepository.findAll().isEmpty()) {
            Categoria salgados = new Categoria();
            salgados.setNome("Salgados");
            salgados.setDescricao("Salgados assados e fritos");
            salgados = categoriaRepository.save(salgados);

            Categoria bebidas = new Categoria();
            bebidas.setNome("Bebidas");
            bebidas.setDescricao("Sucos e refrigerantes");
            bebidas = categoriaRepository.save(bebidas);

            Produto p1 = new Produto();
            p1.setNome("Coxinha de Frango");
            p1.setPrecoVendas(6.50);
            p1.setCategoria(salgados);
            produtoRepository.save(p1);

            Produto p2 = new Produto();
            p2.setNome("Suco de Laranja 500ml");
            p2.setPrecoVendas(5.00);
            p2.setCategoria(bebidas);
            produtoRepository.save(p2);
        }

        for (Produto p : produtoRepository.findAll()) {
            if (estoqueRepository.findByProdutoId(p.getId()).isEmpty()) {
                try {
                    estoqueService.registrarEntrada(p, 10, "Estoque Inicial", p.getPrecoVendas());
                } catch (Exception ignored) {}
            }
        }
    }
}
