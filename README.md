# 🏪 Projeto Cantina — Sistema de Gestão e Autoatendimento

> Solução full-stack para gestão escolar/comercial de cantinas, integrando totem de autoatendimento ao cliente, painel administrativo e API REST em Java com arquitetura em camadas.

---

## 🎯 Sobre o Projeto

O **Projeto Cantina** automatiza o fluxo de vendas e controle de estoque de uma cantina, eliminando filas no atendimento manual e garantindo consistência financeira. 

O sistema opera em dois fluxos integrados:
* **Totem de Autoatendimento:** Interface intuitiva para autenticação, seleção de itens no cardápio e confirmação imediata do pedido.
* **Painel Administrativo:** Gestão centralizada de produtos, reposição de estoque, categorias e controle de pedidos efetuados.

---

## 🛠️ Tecnologias & Ferramentas

| Camada | Tecnologias |
| :--- | :--- |
| **Back-end** | Java 21, Spring Boot (Spring Web, Spring Data JPA, Bean Validation) |
| **Banco de Dados** | MySQL (Modelagem via MySQL Workbench) |
| **Front-end** | HTML5, CSS3, JavaScript (Vanilla com consumo de API REST) |
| **Build & Testes** | Maven, JUnit 5 |

---

## 🏗️ Arquitetura do Sistema

O back-end segue o padrão arquitetural em camadas para separação estrita de responsabilidades:

* `controller/`: Mapeamento das rotas HTTP e endpoints da API REST.
* `service/`: Regras de negócio, cálculos de estoque e processamento de pedidos.
* `repository/`: Camada de persistência e consultas ao MySQL via Spring Data JPA.
* `model/`: Entidades de banco de dados mapeadas via ORM.
* `dto/`: Transferência desacoplada de dados entre requisições e respostas.
* `exception/`: Tratamento centralizado de exceções e erros de validação.
* `config/`: Configurações de CORS, conexão e segurança.

---

## 📁 Estrutura de Diretórios

```text
├── frontend/
│   ├── css/styles.css           # Identidade visual e responsividade
│   ├── js/
│   │   ├── api.js               # Camada de comunicação fetch com o back-end
│   │   ├── admin.js             # Lógica e renderização do painel admin
│   │   └── totem.js             # Lógica do fluxo de autoatendimento
│   ├── admin-dashboard.html     # Painel de controle do operador
│   ├── admin-login.html         # Acesso restrito
│   ├── totem-menu.html          # Cardápio interativo
│   └── totem-sucesso.html       # Confirmação de pedido
├── src/
│   ├── main/java/com/senai/projetoCantina/ # Código-fonte da aplicação
│   └── resources/               # Propriedades da aplicação e credenciais
└── pom.xml                      # Dependências do projeto Maven
