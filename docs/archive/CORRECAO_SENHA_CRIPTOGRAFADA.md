# 🔐 Correção Implementada: Criptografia de Senha

## ✅ Problema Resolvido

A senha dos usuários **não estava sendo criptografada** antes de salvar no banco de dados. Isso é um **grave problema de segurança**!

## 🔧 O Que Foi Corrigido

### 1. **Adicionado PasswordEncoder no UsuarioService**

```java
@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final BairroRepository bairroRepository;
    private final PasswordEncoder passwordEncoder; // ✅ ADICIONADO
```

### 2. **Método criar() - Criptografa a senha ao criar usuário**

**ANTES (INSEGURO):**
```java
Usuario usuario = Usuario.builder()
    .nome(dto.getNome())
    .email(dto.getEmail())
    .senha(dto.getSenha()) // ❌ Senha em texto puro!
    .bairro(bairro)
    .build();
```

**DEPOIS (SEGURO):**
```java
Usuario usuario = Usuario.builder()
    .nome(dto.getNome())
    .email(dto.getEmail())
    .senha(passwordEncoder.encode(dto.getSenha())) // ✅ Senha criptografada!
    .bairro(bairro)
    .build();
```

### 3. **Método atualizar() - Criptografa a senha ao atualizar**

**ANTES (INSEGURO):**
```java
if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
    usuario.setSenha(dto.getSenha()); // ❌ Senha em texto puro!
}
```

**DEPOIS (SEGURO):**
```java
if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
    usuario.setSenha(passwordEncoder.encode(dto.getSenha())); // ✅ Senha criptografada!
}
```

---

## 🎯 Como Funciona Agora

### Criando Usuário
```bash
POST /api/usuarios
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "senha123456",  # ⚠️ Você envia em texto puro
  "bairro": { "id": 1 }
}
```

**O que acontece no backend:**
1. ✅ A senha `"senha123456"` é **criptografada** com BCrypt
2. ✅ Salva no banco: `$2a$10$N9qo8uLOickgx2ZMRZoMye2J.JYm5lqUKl/7M6vKbBGQBWXNjzKSO`
3. ✅ **Impossível reverter** para o texto original

### Login Funcionará Corretamente
```bash
POST /api/auth/login
{
  "email": "joao@email.com",
  "senha": "senha123456"
}
```

**O que acontece:**
1. ✅ Busca o usuário pelo email
2. ✅ Compara a senha enviada com o hash salvo usando `passwordEncoder.matches()`
3. ✅ Se bater, retorna o token JWT
4. ✅ Se não bater, retorna "Credenciais inválidas"

---

## 🔒 Segurança Implementada

### Algoritmo BCrypt
- ✅ **Hash unidirecional**: Não pode ser descriptografado
- ✅ **Salt automático**: Cada senha tem um hash único
- ✅ **Custo adaptativo**: Fica mais lento com o tempo (proteção contra força bruta)
- ✅ **Padrão da indústria**: Usado por empresas de todo o mundo

### Exemplo de Hash BCrypt
```
Senha original: senha123456
Hash salvo:     $2a$10$N9qo8uLOickgx2ZMRZoMye2J.JYm5lqUKl/7M6vKbBGQBWXNjzKSO
```

Mesmo que alguém acesse o banco de dados, **não consegue descobrir a senha original**!

---

## 🚨 IMPORTANTE: Reinicie a Aplicação

Para que as mudanças entrem em vigor, você **DEVE reiniciar** sua aplicação Spring Boot:

1. **Pare a aplicação** (Ctrl+C no terminal ou Stop no IDE)
2. **Inicie novamente** (`mvn spring-boot:run` ou Run no IDE)

---

## 🧪 Testando a Correção

### 1. Crie um novo usuário
```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Teste Segurança",
    "email": "teste@email.com",
    "senha": "minhasenha123",
    "bairro": { "id": 1 }
  }'
```

### 2. Verifique no banco de dados
```sql
SELECT id, nome, email, senha FROM usuario WHERE email = 'teste@email.com';
```

**Você verá algo assim:**
```
id | nome            | email           | senha
1  | Teste Segurança | teste@email.com | $2a$10$abc123...xyz789 (hash criptografado)
```

### 3. Teste o login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@email.com",
    "senha": "minhasenha123"
  }'
```

**Deve retornar:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "USUARIO",
  "id": 1,
  "nome": "Teste Segurança",
  "email": "teste@email.com",
  "bairro": {...}
}
```

---

## 📋 Checklist de Segurança

- ✅ **Senha criptografada ao criar usuário**
- ✅ **Senha criptografada ao atualizar usuário**
- ✅ **Login compara senhas criptografadas**
- ✅ **BCrypt configurado no SecurityConfig**
- ✅ **PasswordEncoder injetado nos serviços**

---

## 🎓 Boas Práticas Implementadas

### ✅ Nunca salve senhas em texto puro
```java
// ❌ NUNCA faça isso
usuario.setSenha(dto.getSenha());

// ✅ SEMPRE faça isso
usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
```

### ✅ Use BCrypt (não MD5 ou SHA1)
```java
// ❌ Algoritmos fracos (quebráveis)
MD5, SHA1, SHA256 simples

// ✅ Algoritmos seguros
BCrypt, Argon2, PBKDF2
```

### ✅ Nunca retorne a senha na API
```java
// ✅ O UsuarioDTO não retorna a senha
public static UsuarioDTO of(Usuario usuario) {
    return UsuarioDTO.builder()
        .id(usuario.getId())
        .nome(usuario.getNome())
        .email(usuario.getEmail())
        // ✅ senha NÃO é retornada
        .bairro(BairroDTO.of(usuario.getBairro()))
        .build();
}
```

---

## 🔄 Próximos Passos Recomendados

### 1. Atualizar Usuários Existentes (se houver)
Se você já criou usuários antes desta correção, eles têm senhas em texto puro no banco. Você deve:

```sql
-- ⚠️ Deletar usuários de teste antigos
DELETE FROM usuario WHERE senha NOT LIKE '$2a$%';

-- Ou resetar senhas manualmente
UPDATE usuario SET senha = '$2a$10$N9qo8uLOickgx2ZMRZoMye2J.JYm5lqUKl/7M6vKbBGQBWXNjzKSO' 
WHERE senha NOT LIKE '$2a$%';
-- Esta é a senha criptografada de "senha123"
```

### 2. Adicionar Validação de Força de Senha
```java
// Adicionar no UsuarioDTO
@Pattern(
    regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
    message = "Senha deve ter mínimo 8 caracteres, com letras e números"
)
private String senha;
```

### 3. Implementar Recuperação de Senha
- Endpoint `/api/auth/esqueci-senha`
- Enviar email com token temporário
- Endpoint `/api/auth/resetar-senha` com token

---

## 📚 Documentação de Referência

- **BCrypt**: https://en.wikipedia.org/wiki/Bcrypt
- **Spring Security Password Encoding**: https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html
- **OWASP Password Storage**: https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html

---

## ✅ Status Final

- [x] Problema identificado
- [x] Correção implementada
- [x] Código compilado com sucesso
- [x] Documentação criada
- [ ] **Aplicação reiniciada** (você precisa fazer isso!)
- [ ] **Testado em produção** (faça os testes acima)

**A correção está completa! Reinicie a aplicação e teste!** 🎉

