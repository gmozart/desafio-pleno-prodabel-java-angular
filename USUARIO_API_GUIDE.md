# API de Usuários - Guia Completo

## 📌 Visão Geral

A API de usuários agora possui validação aprimorada e tratamento de erros amigável. O campo `bairro` é **obrigatório** e deve ser fornecido de duas formas:

## ✅ Formas de Criar Usuário

### 1. Com ID de Bairro Existente (Recomendado)

Use quando o bairro já estiver cadastrado no sistema.

```json
POST /api/usuarios
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "senha123456",
  "bairro": {
    "id": 1
  }
}
```

### 2. Com CEP de Bairro Existente

O sistema busca o bairro pelo CEP. Se não encontrar, cria automaticamente.

```json
POST /api/usuarios
{
  "nome": "Maria Santos",
  "email": "maria@email.com",
  "senha": "senha123456",
  "bairro": {
    "cep": "28010-000"
  }
}
```

### 3. Criando Novo Bairro Junto com Usuário

Fornece todos os dados do bairro para criação automática.

```json
POST /api/usuarios
{
  "nome": "Carlos Oliveira",
  "email": "carlos@email.com",
  "senha": "senha123456",
  "bairro": {
    "nome": "Jardim das Flores",
    "cep": "28100-000",
    "cidade": "Campos dos Goytacazes",
    "estado": "RJ"
  }
}
```

---

## 🔴 Respostas de Erro

### Erro 400 - Validação de Campos

Quando campos obrigatórios estão faltando ou inválidos:

```json
{
  "status": 400,
  "message": "Erro de validação",
  "errors": {
    "nome": "Nome é obrigatório",
    "email": "O email deve ser válido",
    "bairro": "Bairro é obrigatório"
  }
}
```

### Erro 400 - Bairro Não Informado

```json
{
  "status": 400,
  "message": "Bairro é obrigatório"
}
```

### Erro 400 - ID ou CEP do Bairro Obrigatório

```json
{
  "status": 400,
  "message": "É necessário informar o ID ou o CEP do bairro"
}
```

### Erro 404 - Bairro Não Encontrado

```json
{
  "status": 404,
  "message": "Bairro não encontrado com id: 999"
}
```

---

## 📋 Validações Implementadas

### Campos do Usuário
- ✅ **nome**: Obrigatório, não pode estar vazio
- ✅ **email**: Obrigatório, deve ser um email válido
- ✅ **senha**: Opcional na criação, mínimo 8 caracteres
- ✅ **bairro**: Obrigatório, deve conter ID ou dados completos

### Campos do Bairro
- ✅ **id**: Opcional, busca bairro existente
- ✅ **cep**: Formato `00000-000`, busca ou cria bairro
- ✅ **nome**: Obrigatório ao criar novo bairro
- ✅ **cidade**: Obrigatório ao criar novo bairro
- ✅ **estado**: Obrigatório ao criar novo bairro

---

## 🧪 Exemplos com cURL

### Criar usuário com bairro existente (ID)
```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "senha": "senha123456",
    "bairro": {
      "id": 1
    }
  }'
```

### Criar usuário com bairro existente (CEP)
```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Santos",
    "email": "maria@email.com",
    "senha": "senha123456",
    "bairro": {
      "cep": "28010-000"
    }
  }'
```

### Criar usuário e novo bairro
```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Carlos Oliveira",
    "email": "carlos@email.com",
    "senha": "senha123456",
    "bairro": {
      "nome": "Jardim das Flores",
      "cep": "28100-000",
      "cidade": "Campos dos Goytacazes",
      "estado": "RJ"
    }
  }'
```

---

## 🔄 Atualizar Usuário

O endpoint PUT funciona da mesma forma que o POST:

```bash
PUT /api/usuarios/{id}
{
  "nome": "João Silva Atualizado",
  "email": "joao.novo@email.com",
  "senha": "novaSenha123",  # Opcional - só atualiza se fornecida
  "bairro": {
    "id": 2  # Pode mudar o bairro do usuário
  }
}
```

---

## 📊 Bairros Disponíveis para Teste

O sistema já possui 3 bairros cadastrados:

| ID | Nome | CEP | Cidade | Estado |
|----|------|-----|--------|--------|
| 1 | Centro | 28010-000 | Campos dos Goytacazes | RJ |
| 2 | Jardim América | 28020-000 | Campos dos Goytacazes | RJ |
| 3 | Vila Nova | 28040-000 | Campos dos Goytacazes | RJ |

---

## 🎯 Fluxo Recomendado

### Para Desenvolvimento/Testes:
1. **Liste os bairros disponíveis**: `GET /api/bairros`
2. **Crie usuário com ID do bairro**: Use o ID retornado

### Para Produção:
1. **Busque bairro por CEP**: `GET /api/bairros/cep/{cep}`
2. **Se encontrar**: Use o ID retornado
3. **Se não encontrar**: Envie dados completos do bairro na criação do usuário

---

## 🚀 Melhorias Implementadas

### ✅ Validação Aprimorada
- Mensagens de erro claras e específicas
- Validação em múltiplas camadas (DTO + Service)
- Tratamento de erros global

### ✅ Flexibilidade
- Criar usuário com bairro existente (ID)
- Criar usuário com bairro existente (CEP)
- Criar usuário e bairro simultaneamente

### ✅ Respostas HTTP Corretas
- **200 OK**: Busca bem-sucedida
- **201 Created**: Criação bem-sucedida
- **400 Bad Request**: Dados inválidos
- **404 Not Found**: Recurso não encontrado
- **500 Internal Server Error**: Erro interno

---

## 📝 Swagger UI

Documentação interativa completa disponível em:
**http://localhost:8080/swagger-ui.html**

---

## 💡 Dicas

1. **Sempre forneça o bairro**: O campo é obrigatório
2. **Use ID quando possível**: Mais eficiente que buscar por CEP
3. **Valide os dados antes de enviar**: Economiza requisições
4. **Senha é opcional na criação**: Mas recomendamos sempre enviar
5. **CEP deve estar no formato**: `00000-000`

---

## 🔧 Troubleshooting

### "Bairro é obrigatório"
**Solução**: Inclua o campo `bairro` no JSON com pelo menos o ID ou CEP

### "É necessário informar o ID ou o CEP do bairro"
**Solução**: Forneça `{"id": 1}` ou `{"cep": "28010-000"}` ou dados completos

### "Bairro não encontrado com id: X"
**Solução**: Verifique se o ID existe usando `GET /api/bairros`

### "O email deve ser válido"
**Solução**: Use formato válido: `usuario@dominio.com`

