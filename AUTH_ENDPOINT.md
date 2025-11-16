# Endpoint de Autenticação

## POST /api/auth/login

Endpoint para autenticação de usuários e funcionários.

### Request

```json
{
  "email": "alice@email.com",
  "senha": "senha123"
}
```

### Validações

- **email**: Obrigatório, deve ser um email válido
- **senha**: Obrigatória, mínimo de 8 caracteres

### Response - Sucesso (200 OK)

**Para Usuário:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "USUARIO",
  "id": 1,
  "nome": "Alice Silva",
  "email": "alice@email.com",
  "cargo": null,
  "bairro": {
    "id": 1,
    "nome": "Centro",
    "cidade": "Campos dos Goytacazes",
    "estado": "RJ",
    "cep": "28010-000"
  }
}
```

**Para Funcionário:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "FUNCIONARIO",
  "id": 1,
  "nome": "João Carlos",
  "email": "joao@email.com",
  "cargo": "ATENDENTE",
  "bairro": null
}
```

### Response - Erro (500 Internal Server Error)

```json
{
  "message": "Credenciais inválidas"
}
```

## Dados de Teste

### Usuários
- **Email**: alice@email.com | **Senha**: senha123
- **Email**: bruno@email.com | **Senha**: senha123
- **Email**: carla@email.com | **Senha**: senha123

### Funcionários
- **Email**: joao@email.com | **Senha**: senha123 | **Cargo**: ATENDENTE
- **Email**: mariana@email.com | **Senha**: senha123 | **Cargo**: GERENTE
- **Email**: ricardo@email.com | **Senha**: senha123 | **Cargo**: SUPORTE

## Token JWT

O token JWT retornado contém:
- **subject**: Email do usuário/funcionário
- **tipo**: USUARIO ou FUNCIONARIO
- **expiration**: 24 horas a partir da geração

## Testando com cURL

```bash
# Login de usuário
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "alice@email.com",
    "senha": "senha123"
  }'

# Login de funcionário
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "senha": "senha123"
  }'
```

## Swagger UI

Acesse a documentação interativa em: http://localhost:8080/swagger-ui.html

