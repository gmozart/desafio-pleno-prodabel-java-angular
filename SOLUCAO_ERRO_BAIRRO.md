# ⚠️ SOLUÇÃO: Como Enviar Usuário Corretamente

## 🎯 O Erro é Normal - Falta o Campo Bairro!

A mensagem **`{"status":400,"message":"Bairro é obrigatório"}`** significa que o backend está funcionando corretamente, mas você **esqueceu de enviar o campo `bairro` no JSON**.

---

## ✅ JSON CORRETO para Criar Usuário

### Opção 1: Com ID de Bairro Existente (RECOMENDADO)
```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "senha123456",
  "bairro": {
    "id": 1
  }
}
```

### Opção 2: Com CEP (Busca ou Cria Automaticamente)
```json
{
  "nome": "Maria Santos",
  "email": "maria@email.com",
  "senha": "senha123456",
  "bairro": {
    "cep": "28010-000"
  }
}
```

### Opção 3: Criando Novo Bairro
```json
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

## 🔴 Exemplos de JSON ERRADO (que causam o erro)

### ❌ ERRADO - Sem o campo bairro
```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "senha123456"
}
```
**Resultado**: `{"status":400,"message":"Bairro é obrigatório"}`

### ❌ ERRADO - Bairro vazio
```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "senha123456",
  "bairro": {}
}
```
**Resultado**: `{"status":400,"message":"É necessário informar o ID ou o CEP do bairro"}`

### ❌ ERRADO - Bairro null
```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "senha123456",
  "bairro": null
}
```
**Resultado**: `{"status":400,"message":"Bairro é obrigatório"}`

---

## 🧪 Teste com cURL (CORRETO)

```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao.silva@email.com",
    "senha": "senha123456",
    "bairro": {
      "id": 1
    }
  }'
```

---

## 📊 Bairros Disponíveis para Teste

Primeiro, liste os bairros disponíveis:

```bash
curl -X GET http://localhost:8080/api/bairros
```

**Resposta:**
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

Depois use um dos IDs acima (1, 2 ou 3) para criar o usuário.

---

## 💻 Se Você Está Usando Postman/Insomnia

1. **Method**: POST
2. **URL**: `http://localhost:8080/api/usuarios`
3. **Headers**: 
   - `Content-Type: application/json`
4. **Body** (raw, JSON):
```json
{
  "nome": "João Silva",
  "email": "joao.silva@email.com",
  "senha": "senha123456",
  "bairro": {
    "id": 1
  }
}
```

---

## 🎨 Se o Erro é no FRONTEND (Angular)

Se você está fazendo a requisição do Angular, o código deve ser:

```typescript
// usuário.service.ts
criarUsuario(usuario: any) {
  // CERTIFIQUE-SE que o objeto tem o campo bairro
  const payload = {
    nome: usuario.nome,
    email: usuario.email,
    senha: usuario.senha,
    bairro: {
      id: usuario.bairroId  // ou { cep: usuario.bairroCep }
    }
  };
  
  return this.http.post<any>('http://localhost:8080/api/usuarios', payload);
}
```

### Formulário Angular (Exemplo)
```typescript
// usuario-form.component.ts
formulario = this.fb.group({
  nome: ['', Validators.required],
  email: ['', [Validators.required, Validators.email]],
  senha: ['', Validators.required],
  bairroId: ['', Validators.required]  // ⚠️ Campo obrigatório!
});

salvar() {
  if (this.formulario.valid) {
    const usuario = {
      nome: this.formulario.value.nome,
      email: this.formulario.value.email,
      senha: this.formulario.value.senha,
      bairro: {
        id: this.formulario.value.bairroId  // ⚠️ Importante!
      }
    };
    
    this.usuarioService.criarUsuario(usuario).subscribe({
      next: (response) => console.log('Sucesso!', response),
      error: (error) => console.error('Erro:', error)
    });
  }
}
```

---

## 📝 Checklist de Verificação

Antes de enviar a requisição, verifique:

- [ ] O campo `bairro` está presente no JSON?
- [ ] O `bairro` tem pelo menos `id` ou `cep`?
- [ ] O valor do `id` ou `cep` existe no banco?
- [ ] O `Content-Type` está como `application/json`?
- [ ] O JSON está válido (sem vírgulas extras, etc)?

---

## 🎯 Fluxo Recomendado

1. **Primeiro**: Busque a lista de bairros
   ```bash
   GET /api/bairros
   ```

2. **Depois**: Crie o usuário com um ID válido
   ```bash
   POST /api/usuarios
   {
     "nome": "João",
     "email": "joao@email.com",
     "senha": "senha123456",
     "bairro": { "id": 1 }
   }
   ```

---

## ✅ Resposta de Sucesso Esperada

Quando tudo estiver correto, você receberá:

```json
{
  "id": 4,
  "nome": "João Silva",
  "email": "joao@email.com",
  "bairro": {
    "id": 1,
    "nome": "Centro",
    "cep": "28010-000",
    "cidade": "Campos dos Goytacazes",
    "estado": "RJ"
  }
}
```

---

## 🚨 Resumo do Problema

**Não é erro do backend!** O backend está correto e validando como deveria.

**O problema é**: Você está enviando um JSON **sem o campo `bairro`** ou com `bairro: null`.

**Solução**: Sempre envie o campo `bairro` com pelo menos `{ "id": 1 }` ou os dados completos do bairro.

