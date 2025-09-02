# 🏦 Serasa Score API

API REST para cadastro e gerenciamento de pessoas com score e endereço.

## 📋 Índice
- [Tecnologias](#-tecnologias)
- [Funcionalidades](#-funcionalidades)
- [Instalação](#-instalação)
- [Uso da API](#-uso-da-api)
- [CI/CD](#-cicd)

## 🚀 Tecnologias

- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.5** - Framework principal
- **Spring Security + JWT** - Autenticação e autorização
- **H2 Database** - Banco de dados em memória
- **Maven** - Gerenciamento de dependências

## ✨ Funcionalidades

- **Cadastro de pessoas** com validação de dados
- **Integração com ViaCEP** para busca de endereço
- **Listagem paginada** com filtros
- **Gestão de scores** de 0 a 1000 com classificação automática
- **Autenticação JWT** com roles ADMIN/USER
- **Documentação Swagger** interativa

## 🔧 Instalação

### 1️⃣ Clone e execute:
```bash
git clone [url-do-repositorio]
cd serasa-score-api
mvn spring-boot:run
```

### 2️⃣ Acesse:
- **API**: `http://localhost:8080`
- **Swagger**: `http://localhost:8080/swagger-ui/index.html`
- **H2 Console**: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:serasadb`
  - User: `sa`
  - Password: (vazio)

## 🚀 Uso da API

Use o **Swagger UI** ou **Insomnia** para testar a API:
- **Swagger**: `http://localhost:8080/swagger-ui/index.html`
- **Insomnia**: Importe o arquivo `insomnia-collection.json`



## 🚀 CI/CD

O projeto possui CI/CD configurado com GitHub Actions:

- **Testes automáticos** em push e pull requests
- **Build da aplicação** após testes
- **Java 21** configurado


