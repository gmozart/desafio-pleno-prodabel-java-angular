# ============================================
# STAGE 1: Build da aplicação
# ============================================
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

# Metadados
LABEL maintainer="gmozart"
LABEL description="Desafio Pleno Prodabel - Backend Spring Boot"

# Define diretório de trabalho
WORKDIR /app

# Copia arquivos de dependências primeiro (cache layer)
COPY pom.xml .
COPY .mvn .mvn

# Baixa dependências (isso será cacheado se o pom.xml não mudar)
RUN mvn dependency:go-offline -B

# Copia código fonte
COPY src ./src

# Compila a aplicação
RUN mvn clean package -DskipTests -B

# Lista o conteúdo do target para debug
RUN ls -la /app/target/

# ============================================
# STAGE 2: Imagem de produção
# ============================================
FROM eclipse-temurin:17-jre-alpine

# Cria usuário não-root para executar a aplicação
RUN addgroup -g 1000 spring && \
    adduser -D -u 1000 -G spring spring

# Define diretório de trabalho
WORKDIR /app

# Copia o JAR da stage anterior
COPY --from=builder /app/target/*.jar app.jar

# Cria diretório para logs
RUN mkdir -p /app/logs && \
    chown -R spring:spring /app

# Muda para usuário não-root
USER spring:spring

# Expõe a porta da aplicação
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Comando de inicialização
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]