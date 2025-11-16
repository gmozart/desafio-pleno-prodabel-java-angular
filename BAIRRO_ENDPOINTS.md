# Endpoints de Bairro

API REST completa para gerenciamento de bairros.

**Base URL**: `http://localhost:8080/api/bairros`

---

## 📍 Endpoints Disponíveis

### 1. **POST** `/api/bairros` - Criar novo bairro

Cadastra um novo bairro no sistema.

**Request Body:**
```json
{
  "nome": "Jardim das Flores",
  "cep": "28100-000",
  "cidade": "Campos dos Goytacazes",
  "estado": "RJ"
}
```

**Validações:**
- `nome`: Obrigatório
- `cep`: Obrigatório, formato `00000-000`
- `cidade`: Obrigatório
- `estado`: Obrigatório (sigla com 2 caracteres)

**Response (201 Created):**
```json
{
  "id": 4,
  "nome": "Jardim das Flores",
  "cep": "28100-000",
  "cidade": "Campos dos Goytacazes",
  "estado": "RJ"
}
```

**Erros:**
- `400 Bad Request`: Dados inválidos ou CEP já cadastrado
- `400 Bad Request`: "Já existe um bairro cadastrado com o CEP: 28100-000"

---

### 2. **GET** `/api/bairros` - Listar todos os bairros

Retorna a lista completa de bairros cadastrados.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "Centro",
    "cep": "28010-000",
    "cidade": "Campos dos Goytacazes",
    "estado": "RJ"
  },
  {
    "id": 2,
    "nome": "Jardim América",
    "cep": "28020-000",
    "cidade": "Campos dos Goytacazes",
    "estado": "RJ"
  },
  {
    "id": 3,
    "nome": "Vila Nova",
    "cep": "28040-000",
    "cidade": "Campos dos Goytacazes",
    "estado": "RJ"
  }
]
```

---

### 3. **GET** `/api/bairros/{id}` - Buscar bairro por ID

Retorna um bairro específico pelo seu ID.

**Parâmetros:**
- `id` (path): ID do bairro

**Exemplo:** `GET /api/bairros/1`

**Response (200 OK):**
```json
{
  "id": 1,
  "nome": "Centro",
  "cep": "28010-000",
  "cidade": "Campos dos Goytacazes",
  "estado": "RJ"
}
```

**Erros:**
- `404 Not Found`: "Bairro não encontrado com id: 1"

---

### 4. **GET** `/api/bairros/cep/{cep}` - Buscar bairro por CEP

Retorna um bairro específico pelo seu CEP.

**Parâmetros:**
- `cep` (path): CEP do bairro (formato: 00000-000)

**Exemplo:** `GET /api/bairros/cep/28010-000`

**Response (200 OK):**
```json
{
  "id": 1,
  "nome": "Centro",
  "cep": "28010-000",
  "cidade": "Campos dos Goytacazes",
  "estado": "RJ"
}
```

**Erros:**
- `404 Not Found`: "Bairro não encontrado com CEP: 28010-000"

---

### 5. **PUT** `/api/bairros/{id}` - Atualizar bairro

Atualiza os dados de um bairro existente.

**Parâmetros:**
- `id` (path): ID do bairro

**Request Body:**
```json
{
  "nome": "Centro Atualizado",
  "cep": "28010-000",
  "cidade": "Campos dos Goytacazes",
  "estado": "RJ"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "nome": "Centro Atualizado",
  "cep": "28010-000",
  "cidade": "Campos dos Goytacazes",
  "estado": "RJ"
}
```

**Erros:**
- `404 Not Found`: Bairro não encontrado
- `400 Bad Request`: CEP já existe em outro bairro

---

### 6. **DELETE** `/api/bairros/{id}` - Deletar bairro

Remove um bairro do sistema.

**Parâmetros:**
- `id` (path): ID do bairro

**Response (204 No Content):**
Sem corpo de resposta.

**Erros:**
- `404 Not Found`: "Bairro não encontrado com id: 1"

---

## 🧪 Exemplos com cURL

### Criar bairro
```bash
curl -X POST http://localhost:8080/api/bairros \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Jardim das Flores",
    "cep": "28100-000",
    "cidade": "Campos dos Goytacazes",
    "estado": "RJ"
  }'
```

### Listar todos
```bash
curl -X GET http://localhost:8080/api/bairros
```

### Buscar por ID
```bash
curl -X GET http://localhost:8080/api/bairros/1
```

### Buscar por CEP
```bash
curl -X GET http://localhost:8080/api/bairros/cep/28010-000
```

### Atualizar
```bash
curl -X PUT http://localhost:8080/api/bairros/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Centro Atualizado",
    "cep": "28010-000",
    "cidade": "Campos dos Goytacazes",
    "estado": "RJ"
  }'
```

### Deletar
```bash
curl -X DELETE http://localhost:8080/api/bairros/1
```

---

## 📊 Dados de Teste

O sistema já possui 3 bairros cadastrados para testes:

1. **Centro** - CEP: 28010-000
2. **Jardim América** - CEP: 28020-000
3. **Vila Nova** - CEP: 28040-000

---

## 🔍 Validações Implementadas

- ✅ CEP deve estar no formato `00000-000`
- ✅ CEP único (não permite duplicatas)
- ✅ Todos os campos são obrigatórios
- ✅ Estado deve ter exatamente 2 caracteres

---

## 📝 Swagger UI

Acesse a documentação interativa completa em:
**http://localhost:8080/swagger-ui.html**

---

## ⚙️ Tecnologias Utilizadas

- Spring Boot 3.5.6
- Spring Data JPA
- PostgreSQL
- Bean Validation
- Swagger/OpenAPI

