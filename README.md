# 🥪 Sistema de Gestão de Cantina Escolar (Versão 1.4 - Modern UI & Fluxo Pix)

Evolução do sistema de gestão e autoatendimento para cantinas escolares. Esta versão traz uma reformulação visual completa com **Split Layout**, integração com motor de templates **Thymeleaf**, controle visual de estoque em tempo real e módulo de pagamento instantâneo via **Pix**.

---

## 🚀 Novidades da Versão 1.4

* **Identidade Visual Aprimorada:** Design moderno construído com a paleta institucional (azul tecnológico, tons neutros suaves e detalhes em vermelho vibrante), tipografia *Outfit* e layout responsivo.
* **Split Layout:** Telas de entrada (`index.html`, `totem-login.html`, `admin-login.html` e `cadastro.html`) com divisão de tela moderna para desktop e adaptação fluida para dispositivos móveis.
* **Controle de Estoque no Totem:** Badges em tempo real indicando itens disponíveis e desativação automática de botões de compra para produtos sem estoque.
* **Painel Administrativo Completo:** Gestão modular expandida com abas para Pedidos, Produtos, Categorias, Clientes, Tipos de Cliente, Funcionários, Fornecedores, Formas de Pagamento, Estoque e Usuários.
* **Módulo de Pagamento Pix:** Interface dedicada (`pagamento-pix.html`) com renderização dinâmica via Thymeleaf, exibindo valor, QR Code, código Copia e Cola com feedback na área de transferência e modo de confirmação de demonstração.

---

## 🛠️ Tecnologias Utilizadas

### Backend
* **Java 17+**
* **Spring Boot** (Spring MVC, Spring Data JPA, Spring Security)
* **Thymeleaf** (Renderização server-side da tela de pagamento Pix)
* **MySQL** (Persistência relacional de produtos, usuários e vendas)
* **Maven** (Gerenciamento de dependências e build)

### Frontend
* **HTML5 semântico** e **Thymeleaf View Engine**
* **CSS3 moderno** (Variáveis customizadas, Flexbox, Grid e animações de feedback)
* **JavaScript ES6+ Vanilla** (Requisições assíncronas via `fetch` relativas, controle de sessão via `localStorage` e prevenção contra XSS)

---

## 📂 Estrutura de Pastas

```text
projeto-cantina-1.4/
├── src/
│   ├── main/
│   │   ├── java/com/senai/projetoCantina/
│   │   │   ├── config/          # SecurityConfig, DataConfiguration e DataInitializer
│   │   │   ├── controller/      # RestControllers (/api/**) e PagamentoPixController
│   │   │   ├── dto/             # VendaRequestDto, ItemVendaDto, FormaPagamentoDto, etc.
│   │   │   ├── model/           # Entidades JPA (Produto, Categoria, Estoque, Venda, etc.)
│   │   │   ├── repository/      # Interfaces de persistência Spring Data
│   │   │   └── service/         # Regras de negócio e movimentação de estoque
│   │   └── resources/
│   │       ├── static/          # Frontend estático (SPA / Vanilla)
│   │       │   ├── css/
│   │       │   │   └── styles.css
│   │       │   ├── js/
│   │       │   │   ├── api.js
│   │       │   │   ├── admin.js
│   │       │   │   └── totem.js
│   │       │   ├── admin-dashboard.html
│   │       │   ├── admin-login.html
│   │       │   ├── cadastro.html
│   │       │   ├── index.html
│   │       │   ├── totem-login.html
│   │       │   ├── totem-menu.html
│   │       │   └── totem-sucesso.html
│   │       ├── templates/       # Templates processados pelo servidor (Thymeleaf)
│   │       │   └── pagamento-pix.html
│   │       └── application.properties
└── pom.xml
````

---

## Como Executar o Projeto

### 1.Pré-requisitos
* **JDK 17** ou superior instalado
* **MySQL Server** ativo localmente na porta padrão (3306)
* **Git** instalado -> Branch selecionada: Atualizado

### 2. Configuração do Banco de Dados
Verifique os parâmetros de conexão no arquivo application.properties ou na classe DataConfiguration.java:

```text
spring.datasource.url=jdbc:mysql://localhost:3306/cantina_final?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=sua_senha_aqui
````

### 3. Rodando a Aplicação
No terminal, a partir da raiz do projeto:

```text
# Limpar e compilar o projeto
./mvnw clean package

# Iniciar o servidor Spring Boot
./mvnw spring-boot:run
````
A aplicação estará acessível em: http://localhost:8080/index.html

---

## 💳 Fluxo de Demonstração do Pedido e Pix
* Acesso do Aluno: No menu inicial, clique em Totem Aluno e insira a matrícula padrão 1001 (gerada pelo DataInitializer).
* Escolha de Produtos: Navegue pelas categorias e adicione os itens à bandeja (o sistema bloqueia adição acima da quantidade em estoque).
* Seleção de Pagamento: Clique em Finalizar Pedido e escolha Pix.
* Tela de Cobrança: Você será direcionado para o template do Pix, com valor atualizado e código Copia e Cola.
* Confirmação: Utilize o botão Confirmar Pagamento (Modo Apresentação) para simular a liquidação imediata da venda no sistema.
