# 🥪 Sistema de Gestão de Cantina Escolar (Versão 1.0)

Sistema de autoatendimento e retaguarda desenvolvido para cantinas escolares, com arquitetura dividida em uma API RESTful em **Java / Spring Boot** e uma interface interativa em **HTML5, CSS3 e JavaScript Vanilla**.

---
## 🌿 Estrutura de Branches do Repositório

O projeto está dividido em duas branches para evidenciar a sua evolução arquitetural e visual:

* **`main` (Versão 1.0):** Versão inicial focada no fluxo essencial da aplicação, com interface estática Vanilla JS simples, integração RESTful direta e endpoints de gestão básica.
* **`v2-frontend-ia-pix` (Versão 1.4):** Versão atualizada com redesign completo (*Split Layout*), controlo dinâmico de stock no quiosque, painel administrativo expandido e módulo de pagamento instantâneo via Pix com Thymeleaf.

---

## 📌 Visão Geral da Versão 1

A primeira versão foca no fluxo essencial de atendimento e gestão básica de itens:
* **Totem de Autoatendimento:** Identificação de aluno via matrícula, listagem dinâmica de produtos categorizados, montagem de bandeja e registro do pedido.
* **Painel Administrativo:** Autenticação de operadores/administradores e módulo para cadastro e visualização de produtos sincronizados com a base de dados.
* **Backend Integrado:** Endpoints REST documentados para produtos, autenticação, verificação de clientes e persistência de vendas no MySQL.

---

## 🛠️ Tecnologias Utilizadas

### Backend
* **Java 17+**
* **Spring Boot** (Spring Web, Spring Data JPA, Spring Security)
* **MySQL** (Persistência relacional)
* **Maven** (Gerenciador de dependências)

### Frontend
* **HTML5 semântico**
* **CSS3 moderno** (Variáveis nativas, Flexbox, Grid e layout responsivo para totens touchscreen)
* **JavaScript (ES6+) Vanilla** (Comunicação assíncrona via `fetch`, manipulação do DOM e proteção contra injeções de script)

---

## 📂 Estrutura do Projeto

```text
projeto-cantina/
├── src/
│   ├── main/
│   │   ├── java/com/senai/projetoCantina/
│   │   │   ├── config/          # Configurações de segurança, CORS e carga inicial
│   │   │   ├── controller/      # Endpoints REST (/api/produtos, /api/vendas, etc.)
│   │   │   ├── dto/             # Objetos de transferência de dados (VendaRequest, Item, etc.)
│   │   │   ├── model/           # Entidades JPA (Produto, Categoria, Cliente, Venda)
│   │   │   ├── repository/      # Interfaces Spring Data JPA
│   │   │   └── service/         # Regras de negócio e movimentação de estoque
│   │   └── resources/
│   │       ├── static/          # Frontend da Versão 1 (arquivos estáticos)
│   │       │   ├── css/
│   │       │   │   └── styles.css
│   │       │   ├── js/
│   │       │   │   ├── api.js
│   │       │   │   ├── admin.js
│   │       │   │   └── totem.js
│   │       │   ├── admin-dashboard.html
│   │       │   ├── admin-login.html
│   │       │   ├── index.html
│   │       │   ├── totem-login.html
│   │       │   ├── totem-menu.html
│   │       │   └── totem-sucesso.html
│   │       └── application.properties
└── pom.xml
````

---

## Como Executar o Projeto

### 1.Pré-requisitos
* **JDK 17** ou superior instalado
* **MySQL Server** ativo localmente na porta padrão (3306)
* **Git** instalado.

### 2. Configuração do Banco de Dados
Certifique-se de que as credenciais em src/main/resources/application.properties (ou na classe DataConfiguration.java) correspondam ao seu ambiente MySQL local:

```text
spring.datasource.url=jdbc:mysql://localhost:3306/cantina_final?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=sua_senha_aqui
spring.jpa.hibernate.ddl-auto=update
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
