# Desafio Pleno Java + Angular

![Build](https://img.shields.io/badge/build-passing-brightgreen)
![Maven](https://img.shields.io/badge/maven-3.9.0-blue)
![Java](https://img.shields.io/badge/java-17-orange)
![Spring Boot](https://img.shields.io/badge/spring%20boot-3.5.6-brightgreen)
![PostgreSQL](https://img.shields.io/badge/postgresql-15-blue)
![Swagger](https://img.shields.io/badge/swagger-documented-brightgreen)
![Flyway](https://img.shields.io/badge/flyway-9.22.0-blue)
![Lombok](https://img.shields.io/badge/lombok-1.18.30-red)
![JUnit5](https://img.shields.io/badge/junit-5.11.3-blue)
![H2 Database](https://img.shields.io/badge/h2-database-lightgrey)
![JaCoCo](https://img.shields.io/badge/jacoco-0.8.12-yellowgreen)
---

## Descrição
Aplicação desenvolvida como desafio pleno para **gestão de atendimentos**.  
O backend é feito em **Spring Boot** com **JPA/Hibernate** e banco de dados **PostgreSQL**, e o frontend em **Angular**.

Gerencia **Usuários**, **Solicitações** e **Funcionários**, permitindo CRUD completo, além de métricas de atendimentos por bairro.

---

## Documentação

A documentação completa do projeto está organizada no diretório [`docs/`](docs/):

- **[Índice da Documentação](docs/index.md)** - Ponto de entrada para toda a documentação
- **[API de Autenticação](docs/api/auth.md)** - Endpoints de login e autenticação
- **[API de Bairros](docs/api/bairro.md)** - Gerenciamento completo de bairros
- **[API de Usuários](docs/api/usuario.md)** - Guia de uso da API de usuários

Para documentação interativa da API, acesse o **Swagger UI** em: http://localhost:8080/swagger-ui/index.html

---

## Tecnologias Utilizadas

- **Backend:** Spring Boot 3.5.6, Java 17
- **Banco de Dados:** PostgreSQL (via Docker)
- **Migrations:** Flyway
- **Documentação da API:** Swagger (Springdoc OpenAPI)
- **Frontend:** Angular
- **Segurança:** Spring Security
- **Validação:** Bean Validation
- **Ferramentas:** Maven, Docker

---

## Estrutura do Projeto

```
desafio-pleno-java-angular/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── br/gov/prodabel/desafio/
│   │   │   │       ├── config/        # Configurações gerais do Spring Boot
│   │   │   │       ├── controller/    # Controllers REST
│   │   │   │       ├── domain/        # Entidades JPA (Models)
│   │   │   │       ├── dto/           # Objetos de transferência de dados
│   │   │   │       ├── exception/     # Tratamento de exceções personalizadas
│   │   │   │       ├── repository/    # Repositórios JPA
│   │   │   │       └── service/       # Lógica de negócio
│   │   │   └── resources/
│   │   │       ├── application.properties   # Configurações do Spring Boot
│   │   │       ├── db/
│   │   │       │   └── migration/     # Scripts Flyway
│   │   │       └── static/            # Recursos estáticos, se houver
│   │   └── test/
│   │       └── java/br/gov/prodabel/desafio/  # Testes unitários e de integração
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/    # Componentes Angular
│   │   │   ├── services/      # Serviços Angular (HTTP)
│   │   │   ├── models/        # Models/Interfaces TypeScript
│   │   │   └── pages/         # Páginas da aplicação
│   │   ├── assets/            # Recursos (imagens, CSS, etc.)
│   │   └── environments/      # Configurações por ambiente
│   ├── angular.json
│   ├── package.json
│   └── tsconfig.json
├── README.md
└── .gitignore

```

## Configuração do Banco de Dados 🐳 Docker

Arquivo `docker-compose.yml`:

```yaml
services:
  postgres:
    image: postgres:15
    container_name: desafio-postgres
    restart: always
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: gestao_atendimentos
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

## Configuração do Backend

Arquivo `application.properties`:

```json
spring.application.name=desafio-pleno-java-angular

spring.datasource.url=jdbc:postgresql://localhost:5432/gestao_atendimentos
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
        
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.jpa.hibernate.ddl-auto=none
```
## Testes

Foram implementados testes ``` unitários ``` para validar a lógica de negócio do backend.

Foram implementados testes de ``` integração ``` para garantir que os endpoints REST funcionam corretamente com o banco de dados H2 em memória durante os testes.

## SWAGGER UI

http://localhost:8080/swagger-ui/index.html#/