# Desafio Pleno Java + Angular

![Build](https://img.shields.io/badge/build-passing-brightgreen)
![Maven](https://img.shields.io/badge/maven-3.9.0-blue)
![Java](https://img.shields.io/badge/java-17-orange)
![Spring Boot](https://img.shields.io/badge/spring%20boot-3.5.6-brightgreen)
![PostgreSQL](https://img.shields.io/badge/postgresql-15-blue)
![Swagger](https://img.shields.io/badge/swagger-documented-brightgreen)

---

## Descrição
Aplicação desenvolvida como desafio pleno para **gestão de atendimentos**.  
O backend é feito em **Spring Boot** com **JPA/Hibernate** e banco de dados **PostgreSQL**, e o frontend em **Angular**.

Gerencia **Usuários**, **Solicitações** e **Funcionários**, permitindo CRUD completo, além de métricas de atendimentos por bairro.

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

desafio-pleno-java-angular/
├── backend/
│ ├── src/
│ │ ├── main/
│ │ │ ├── java/
│ │ │ │ └── br/gov/prodabel/desafio/
│ │ │ │ ├── config/ # Configurações gerais do Spring Boot
│ │ │ │ ├── controller/ # Controllers REST
│ │ │ │ ├── domain/ # Entidades JPA (Models)
│ │ │ │ ├── dto/ # Objetos de transferência de dados
│ │ │ │ ├── exception/ # Tratamento de exceções personalizadas
│ │ │ │ ├── repository/ # Repositórios JPA
│ │ │ │ └── service/ # Lógica de negócio
│ │ │ └── resources/
│ │ │ ├── application.properties # Configurações do Spring Boot
│ │ │ ├── db/
│ │ │ │ └── migration/ # Scripts Flyway
│ │ │ └── static/ # Recursos estáticos, se houver
│ │ └── test/
│ │ └── java/
│ │ └── br/gov/prodabel/desafio/ # Testes unitários e de integração
│ └── pom.xml
├── frontend/
│ ├── src/
│ │ ├── app/
│ │ │ ├── components/ # Componentes Angular
│ │ │ ├── services/ # Serviços Angular (HTTP)
│ │ │ ├── models/ # Models/Interfaces TS
│ │ │ └── pages/ # Páginas da aplicação
│ │ ├── assets/ # Recursos (imagens, CSS, etc.)
│ │ └── environments/ # Configurações por ambiente
│ ├── angular.json
│ ├── package.json
│ └── tsconfig.json
├── README.md
└── .gitignore



---

## Configuração do Banco de Dados 🐳 Docker

Arquivo `docker-compose.yml`:

```yaml
version: "3.8"
services:
  postgres:
    image: postgres:15
    container_name: desafio-postgres
    environment:
      POSTGRES_DB: gestao_atendimentos
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
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
spring.jpa.hibernate.ddl-auto=none

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
```
## SWAGGER UI

http://localhost:8080/swagger-ui/index.html#/