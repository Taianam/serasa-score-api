# CI/CD Configuration

Este diretório contém as configurações de CI/CD para o projeto Serasa Score API.

## Workflows

### ci-cd.yml
Pipeline principal que executa:
- **Testes**: Testes unitários e de integração
- **Build**: Compilação da aplicação
- **Docker**: Build e push da imagem Docker
- **Security Scan**: Análise de vulnerabilidades OWASP
- **Code Quality**: Análise SonarQube e Checkstyle
- **Deploy**: Deploy automático para produção

## Secrets Necessários

Configure os seguintes secrets no GitHub:

### Docker Hub
- `DOCKER_USERNAME`: Seu usuário do Docker Hub
- `DOCKER_PASSWORD`: Sua senha/token do Docker Hub

### SonarQube (Opcional)
- `SONAR_TOKEN`: Token do SonarCloud/SonarQube

## Como Usar

1. Faça push para as branches `main` ou `develop`
2. O pipeline será executado automaticamente
3. Para Pull Requests, apenas os testes serão executados
4. Para a branch `main`, o deploy será executado após todos os testes passarem

## Configurações Locais

### Executar testes localmente
```bash
mvn test
```

### Executar análise de qualidade
```bash
mvn checkstyle:check
mvn jacoco:report
```

### Executar análise de segurança
```bash
mvn org.owasp:dependency-check-maven:check
```

### Build da aplicação
```bash
mvn clean package
```

## Estrutura dos Jobs

1. **test**: Executa todos os testes e gera relatórios de cobertura
2. **build**: Compila a aplicação e gera o JAR
3. **docker**: Build e push da imagem Docker (apenas na branch main)
4. **security-scan**: Análise de vulnerabilidades
5. **code-quality**: Análise de qualidade de código
6. **deploy**: Deploy para produção (apenas na branch main)
