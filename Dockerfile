# Multi-stage build para otimizar o tamanho da imagem
FROM maven:3.9.4-openjdk-21-slim AS builder

# Definir diretório de trabalho
WORKDIR /app

# Copiar arquivos de configuração do Maven
COPY pom.xml .
COPY src ./src

# Compilar a aplicação (pula testes para build mais rápido)
RUN mvn clean package -DskipTests

# Segunda stage: runtime
FROM openjdk:21-jre-slim

# Metadados da imagem
LABEL maintainer="Serasa Score API Team"
LABEL description="API REST para gerenciamento de scores de usuários"
LABEL version="1.0.0"

# Criar usuário não-root para segurança
RUN groupadd -r appgroup && useradd -r -g appgroup appuser

# Definir diretório de trabalho
WORKDIR /app

# Copiar o JAR da aplicação do stage anterior
COPY --from=builder /app/target/serasa-score-api-*.jar app.jar

# Alterar propriedade do arquivo para o usuário não-root
RUN chown appuser:appgroup app.jar

# Instalar curl para health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Mudar para usuário não-root
USER appuser

# Expor a porta da aplicação
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Configurações de JVM otimizadas
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport"

# Comando de inicialização
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
