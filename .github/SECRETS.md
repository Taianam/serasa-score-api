# Configuração de Secrets

Para que o CI/CD funcione corretamente, você precisa configurar os seguintes secrets no GitHub:

## Como Configurar Secrets

1. Vá para o seu repositório no GitHub
2. Clique em **Settings** (Configurações)
3. No menu lateral, clique em **Secrets and variables** > **Actions**
4. Clique em **New repository secret**
5. Adicione cada secret listado abaixo

## Secrets Obrigatórios

### Docker Hub
```
Nome: DOCKER_USERNAME
Valor: seu_usuario_dockerhub
```

```
Nome: DOCKER_PASSWORD
Valor: sua_senha_ou_token_dockerhub
```

## Secrets Opcionais

### SonarQube/SonarCloud
```
Nome: SONAR_TOKEN
Valor: seu_token_sonarcloud
```

Para obter o token do SonarCloud:
1. Acesse https://sonarcloud.io
2. Faça login com sua conta GitHub
3. Vá em **My Account** > **Security**
4. Gere um novo token

## Verificação

Após configurar os secrets, faça um push para a branch `main` e verifique se:
- Os testes são executados
- A imagem Docker é buildada e enviada
- O deploy é executado (se configurado)

## Troubleshooting

### Erro de autenticação Docker
- Verifique se o `DOCKER_USERNAME` e `DOCKER_PASSWORD` estão corretos
- Para maior segurança, use um token de acesso ao invés da senha

### Erro SonarQube
- Verifique se o `SONAR_TOKEN` está correto
- Certifique-se de que o projeto está configurado no SonarCloud

### Pipeline não executa
- Verifique se o arquivo `.github/workflows/ci-cd.yml` está na branch correta
- Verifique se não há erros de sintaxe no YAML
