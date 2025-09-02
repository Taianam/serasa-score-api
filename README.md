# 🏦 Serasa Score API

API REST completa para cadastro e gerenciamento de pessoas com score e endereço, desenvolvida seguindo as melhores práticas de desenvolvimento com Spring Boot.

**Separação clara entre:**
- **User**: Usuários de autenticação (ADMIN/USER)
- **Pessoa**: Pessoas cadastradas com score e endereço

## 📋 Índice
- [Tecnologias](#-tecnologias)
- [Funcionalidades](#-funcionalidades)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Configuração](#-instalação-e-configuração)
- [Uso da API](#-uso-da-api)
- [Arquitetura](#-arquitetura)
- [Design Patterns](#-design-patterns)
- [Segurança](#-segurança)
- [Testes](#-testes)
- [Documentação da API](#-documentação-da-api)

## 🚀 Tecnologias

- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.5** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security + JWT** - Autenticação e autorização
- **Spring Validation** - Validação de dados
- **OpenFeign** - Cliente HTTP para integração com APIs externas
- **Swagger/OpenAPI 3** - Documentação da API
- **H2 Database** - Banco de dados em memória
- **Maven** - Gerenciamento de dependências
- **JUnit 5 + Mockito** - Testes unitários e de integração

## ✨ Funcionalidades

### 👥 Gestão de Pessoas
- ✅ **Cadastro de pessoas** com validação completa de dados
- ✅ **Integração automática com ViaCEP** para busca de endereço
- ✅ **Listagem paginada** com filtros por nome, idade e CEP
- ✅ **Atualização de dados** com validação de duplicidade
- ✅ **Exclusão lógica** (soft delete) preservando histórico
- ✅ **Gestão de scores** de 0 a 1000 com classificação automática

### 👤 Gestão de Usuários de Autenticação
- ✅ **Criação de usuários** com roles ADMIN/USER
- ✅ **Listagem de usuários** com paginação
- ✅ **Atualização de dados** de usuários
- ✅ **Ativação/Desativação** de usuários
- ✅ **Exclusão lógica** (soft delete) de usuários
- ✅ **Validação de username único**

### 🔐 Segurança e Autenticação
- ✅ **Autenticação JWT** com tokens seguros
- ✅ **Autorização baseada em roles** (ADMIN/USER)
- ✅ **Criptografia de senhas** com BCrypt
- ✅ **Proteção de endpoints** com Spring Security

### 📊 Sistema de Scores
- ✅ **Classificação automática** do score:
  - **Insuficiente**: 0-200
  - **Inaceitável**: 201-500  
  - **Aceitável**: 501-700
  - **Recomendável**: 701-1000
- ✅ **Histórico de scores** por pessoa
- ✅ **Consulta de descrição** do score

### 🛠️ Recursos Técnicos
- ✅ **API REST** com padrões RESTful
- ✅ **Documentação Swagger** interativa
- ✅ **Validação de dados** com Bean Validation
- ✅ **Tratamento de exceções** centralizado
- ✅ **Testes unitários e de integração** com alta cobertura
- ✅ **Banco H2 em memória** com dados iniciais

## 📋 Pré-requisitos

- **Java JDK 21** ou superior
- **Maven 3.6+** 
- **IDE** de sua preferência (recomendado: IntelliJ IDEA ou VS Code)
- **Git** para versionamento

## 🔧 Instalação e Configuração

### 1️⃣ Clone o repositório:
```bash
git clone [url-do-repositorio]
cd serasa-score-api
```

### 2️⃣ Compile e execute os testes:
```bash
mvn clean install
```

### 3️⃣ Execute a aplicação:
```bash
mvn spring-boot:run
```

### 4️⃣ Acesse a aplicação:
- **API Base URL**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **H2 Console**: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:serasadb`
  - User: `sa`
  - Password: (vazio)

### 🔧 Variáveis de Ambiente

A aplicação suporta configuração através de variáveis de ambiente:

#### ViaCEP Integration
```bash
# URL da API do ViaCEP (opcional - padrão: https://viacep.com.br/ws)
export VIA_CEP_URL=https://viacep.com.br/ws
```

**Exemplo de uso:**
```bash
# Definir variável de ambiente e executar
export VIA_CEP_URL=https://viacep.com.br/ws
mvn spring-boot:run
```

**Nota:** Se a variável `VIA_CEP_URL` não for definida, a aplicação usará o valor padrão `https://viacep.com.br/ws`.

## 🚀 Uso da API

### 🔑 Autenticação

Primeiro, faça login para obter um token JWT:

```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "123456"
  }'
```

**Usuários pré-cadastrados:**
- **Admin**: `admin` / `123456` (role: ADMIN)
- **User**: `user` / `123456` (role: USER)

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 📝 Exemplos de Uso

#### Criar Pessoa (ADMIN apenas)
```bash
curl -X POST http://localhost:8080/api/pessoas \
  -H "Authorization: Bearer SEU_TOKEN_JWT" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "idade": 30,
    "cep": "01310100",
    "telefone": "11999999999",
    "score": 750
  }'
```

#### Listar Pessoas com Paginação e Filtros
```bash
# Listagem simples
curl -X GET http://localhost:8080/api/pessoas \
  -H "Authorization: Bearer SEU_TOKEN_JWT"

# Listagem paginada com filtros
curl -X GET "http://localhost:8080/api/pessoas/paginado?nome=João&idade=30&page=0&size=5" \
  -H "Authorization: Bearer SEU_TOKEN_JWT"
```

#### Buscar Score de Pessoa
```bash
curl -X GET http://localhost:8080/api/scores/pessoas/1/ultimo \
  -H "Authorization: Bearer SEU_TOKEN_JWT"
```

#### Criar Usuário de Autenticação (ADMIN apenas)
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer SEU_TOKEN_JWT" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "novo_user",
    "password": "123456",
    "role": "USER"
  }'
```

#### Listar Usuários de Autenticação (ADMIN apenas)
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer SEU_TOKEN_JWT"
```

## 🏗️ Arquitetura

O projeto segue uma **arquitetura em camadas (Layered Architecture)** bem definida, promovendo separação de responsabilidades e facilidade de manutenção:

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                      │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │ Controllers │  │    DTOs     │  │   Exception Handler │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                     BUSINESS LAYER                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │  Services   │  │   Mappers   │  │     Validations     │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                   PERSISTENCE LAYER                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │Repositories │  │  Entities   │  │    H2 Database      │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                   INTEGRATION LAYER                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │OpenFeign    │  │   ViaCEP    │  │   External APIs     │ │
│  │  Clients    │  │    API      │  │                     │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### 📦 Estrutura de Pacotes

```
src/main/java/com/serasa/scoresapi/
├── 🎯 controller/          # Camada de Apresentação
├── 🏢 service/             # Camada de Negócio
│   └── impl/              # Implementações dos serviços
├── 🗄️ repository/          # Camada de Persistência
├── 🏗️ domain/              # Entidades e Modelos
│   ├── client/            # Clientes de APIs externas
│   ├── factory/           # Factories
│   └── strategy/          # Strategy Pattern
├── 📄 dto/                 # Data Transfer Objects
│   ├── request/           # DTOs de entrada
│   └── response/          # DTOs de saída
├── 🔄 mapper/              # Conversores de dados
├── 🔐 security/            # Configurações de segurança
└── ⚙️ config/              # Configurações gerais
```

## 🎨 Design Patterns

O projeto implementa diversos **Design Patterns** para garantir código limpo, extensível e manutenível:

### 1. **Strategy Pattern** 🎯
**Localização**: `domain/strategy/`
**Propósito**: Classificação dinâmica de scores

```java
// Interface Strategy
public interface ScoreClassificacaoStrategy {
    String getDescricao();
    boolean aplicavel(int score);
}

// Implementações concretas
@Component
public class ScoreInsuficienteStrategy implements ScoreClassificacaoStrategy {
    public boolean aplicavel(int score) { return score >= 0 && score <= 200; }
    public String getDescricao() { return "Insuficiente"; }
}
```

### 2. **Factory Pattern** 🏭
**Localização**: `domain/factory/`
**Propósito**: Criação de estratégias de classificação

```java
@Component
public class ScoreClassificacaoFactory {
    public ScoreClassificacaoStrategy criarEstrategia(int score) {
        // Retorna a estratégia apropriada baseada no score
    }
}
```

### 3. **Repository Pattern** 🗄️
**Localização**: `repository/`
**Propósito**: Abstração do acesso a dados

```java
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Page<Usuario> findUsuariosComFiltros(String nome, Integer idade, String cep, Pageable pageable);
}
```

### 4. **DTO Pattern** 📄
**Localização**: `dto/`
**Propósito**: Transferência segura de dados entre camadas

```java
// Request DTO com validações
public class UsuarioRequest {
    @NotBlank @Size(min = 3, max = 100)
    private String nome;
    
    @Min(0) @Max(1000)
    private Integer score;
}

// Response DTO
public class UsuarioResponse {
    private Long id;
    private String nome;
    private String scoreDescricao;
}
```

### 5. **Mapper Pattern** 🔄
**Localização**: `mapper/`
**Propósito**: Conversão entre entidades e DTOs

```java
@Component
public class UsuarioMapper {
    public Usuario toEntity(UsuarioRequest request) { /* ... */ }
    public UsuarioResponse toResponse(Usuario usuario, Score ultimoScore) { /* ... */ }
}
```

### 6. **Builder Pattern** (via Lombok) 🔨
**Propósito**: Construção fluente de objetos

### 7. **Dependency Injection** 💉
**Propósito**: Inversão de controle e baixo acoplamento

```java
@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ViaCepClient viaCepClient;
    private final ScoreService scoreService;
    
    // Injeção via construtor
    public UsuarioServiceImpl(UsuarioRepository repo, ViaCepClient client, ScoreService service) {
        this.usuarioRepository = repo;
        this.viaCepClient = client;
        this.scoreService = service;
    }
}
```

## 🔐 Segurança

### Arquitetura de Segurança

```
┌─────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Cliente   │───▶│  JWT Filter     │───▶│   Controller    │
└─────────────┘    │  Authentication │    └─────────────────┘
                   └─────────────────┘             │
                            │                      ▼
                   ┌─────────────────┐    ┌─────────────────┐
                   │   JWT Utils     │    │   @PreAuthorize │
                   │                 │    │   ROLE_ADMIN    │
                   └─────────────────┘    │   ROLE_USER     │
                                         └─────────────────┘
```

### Componentes de Segurança

- **JWT Authentication Filter**: Intercepta e valida tokens
- **JWT Authorization Filter**: Autoriza acesso baseado em roles
- **Custom UserDetailsService**: Carrega dados do usuário para autenticação
- **Password Encoder**: Criptografia BCrypt para senhas
- **Method Security**: Autorização a nível de método com `@PreAuthorize`

### Fluxo de Autenticação

1. **Login** → Validação de credenciais → Geração de token JWT
2. **Requisição** → Validação de token → Extração de roles → Autorização
3. **Acesso** → Verificação de permissões → Execução do endpoint

## 🧪 Testes

### Estratégia de Testes

```
┌─────────────────────────────────────────────────────────────┐
│                    TESTES UNITÁRIOS                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │  Services   │  │   Mappers   │  │     Strategies      │ │
│  │   Tests     │  │    Tests    │  │       Tests         │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                  TESTES DE INTEGRAÇÃO                      │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │ Controller  │  │  Security   │  │      Database       │ │
│  │   Tests     │  │    Tests    │  │       Tests         │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### Comandos de Teste

```bash
# Executar todos os testes
mvn test

# Executar testes com cobertura
mvn verify

# Executar teste específico
mvn test -Dtest=UsuarioServiceTest

# Relatório de cobertura
mvn jacoco:report
```

### Cobertura de Testes
- **Meta**: Mínimo 80% de cobertura
- **Relatórios**: `target/site/jacoco/index.html`
- **Tipos**: Unitários, Integração, Segurança

## 📖 Documentação da API

### Swagger UI
Acesse a documentação interativa em:
```
http://localhost:8080/swagger-ui/index.html
```

### Endpoints Principais

| Método | Endpoint | Descrição | Autorização |
|--------|----------|-----------|-------------|
| `POST` | `/api/login` | Autenticação | Público |
| `POST` | `/api/pessoas` | Criar pessoa | ADMIN |
| `GET` | `/api/pessoas` | Listar pessoas | ADMIN/USER |
| `GET` | `/api/pessoas/paginado` | Listar com filtros | ADMIN/USER |
| `GET` | `/api/pessoas/{id}` | Buscar por ID | ADMIN/USER |
| `PUT` | `/api/pessoas/{id}` | Atualizar pessoa | ADMIN |
| `DELETE` | `/api/pessoas/{id}` | Excluir pessoa | ADMIN |
| `GET` | `/api/scores/pessoas/{id}/ultimo` | Último score | ADMIN/USER |
| `POST` | `/api/users` | Criar usuário auth | ADMIN |
| `GET` | `/api/users` | Listar usuários auth | ADMIN |
| `GET` | `/api/users/{id}` | Buscar usuário auth | ADMIN |
| `PUT` | `/api/users/{id}` | Atualizar usuário auth | ADMIN |
| `DELETE` | `/api/users/{id}` | Excluir usuário auth | ADMIN |
| `PUT` | `/api/users/{id}/ativar` | Ativar usuário | ADMIN |
| `PUT` | `/api/users/{id}/desativar` | Desativar usuário | ADMIN |

## 🚀 Deploy e Produção

### Variáveis de Ambiente
```bash
# Configurações de segurança
JWT_SECRET=seu-jwt-secret-super-seguro-aqui
JWT_EXPIRATION=86400000

# Configurações de banco (para produção)
DB_URL=jdbc:postgresql://localhost:5432/serasadb
DB_USERNAME=usuario
DB_PASSWORD=senha
```

### Docker (Opcional)
```dockerfile
FROM openjdk:21-jre-slim
COPY target/serasa-score-api-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 📝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

---

⭐ **Desenvolvido com ❤️ seguindo as melhores práticas de desenvolvimento Java/Spring Boot**
