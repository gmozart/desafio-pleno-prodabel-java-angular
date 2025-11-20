# 🐳 Guia de Uso do Docker - Desafio Prodabel

Este guia explica como usar o Docker para executar o projeto localmente.

## 📋 Pré-requisitos

- Docker Engine 20.10+
- Docker Compose 2.0+

Para verificar se tem instalado:
```bash
docker --version
docker compose version
```

## 🚀 Como Usar

### 1. Configurar Variáveis de Ambiente

Copie o arquivo de exemplo e ajuste conforme necessário:

```bash
cp .env.example .env
```

Edite o arquivo `.env` com suas configurações.

### 2. Iniciar Todos os Serviços

Para iniciar **Backend + PostgreSQL**:

```bash
docker compose up -d
```

Para iniciar **incluindo PgAdmin** (ferramenta de gerenciamento):

```bash
docker compose --profile dev up -d
```

### 3. Acompanhar os Logs

Ver logs de todos os serviços:
```bash
docker compose logs -f
```

Ver logs apenas do backend:
```bash
docker compose logs -f backend
```

Ver logs apenas do banco:
```bash
docker compose logs -f postgres
```

### 4. Verificar Status dos Containers

```bash
docker compose ps
```

### 5. Parar os Serviços

Parar sem remover os dados:
```bash
docker compose stop
```

Parar e remover containers (mantém os dados nos volumes):
```bash
docker compose down
```

Parar e remover TUDO (incluindo volumes/dados):
```bash
docker compose down -v
```

## 🔧 Comandos Úteis

### Reconstruir a Aplicação

Se você fez alterações no código e quer reconstruir:

```bash
docker compose build --no-cache backend
docker compose up -d backend
```

### Acessar o Shell do Backend

```bash
docker exec -it desafio-backend sh
```

### Acessar o PostgreSQL via CLI

```bash
docker exec -it desafio-postgres psql -U postgres -d gestao_atendimentos
```

### Ver Logs de Erro do Backend

```bash
docker exec -it desafio-backend cat /app/logs/spring.log
```

### Limpar Tudo (Reset Completo)

```bash
docker compose down -v
docker system prune -a --volumes -f
```

## 🌐 URLs de Acesso

Após iniciar os serviços, você pode acessar:

- **Backend API**: http://localhost:8080
- **PgAdmin** (se iniciado com profile dev): http://localhost:8081
  - Email: admin@admin.com
  - Senha: admin

### Conectar PgAdmin ao PostgreSQL

No PgAdmin, adicione um novo servidor com:
- **Host**: postgres
- **Port**: 5432
- **Database**: gestao_atendimentos
- **Username**: postgres
- **Password**: postgres

## 🐛 Troubleshooting

### Porta já está em uso

Se receber erro que a porta já está sendo usada, você pode:

1. Mudar a porta no arquivo `.env`:
```bash
BACKEND_PORT=8081
DB_PORT=5433
```

2. Ou parar o serviço que está usando a porta

### Backend não inicia

Verifique os logs:
```bash
docker compose logs backend
```

Verifique se o PostgreSQL está saudável:
```bash
docker compose ps postgres
```

### Limpar cache do Maven

Se tiver problemas de build:
```bash
docker compose build --no-cache backend
```

### Banco de dados não conecta

1. Verifique se o PostgreSQL está rodando:
```bash
docker compose ps postgres
```

2. Verifique os logs do PostgreSQL:
```bash
docker compose logs postgres
```

3. Teste a conexão manualmente:
```bash
docker exec -it desafio-postgres pg_isready -U postgres
```

## 📊 Health Checks

O Docker Compose está configurado com health checks:

- **PostgreSQL**: Verifica se o banco está aceitando conexões
- **Backend**: Verifica o endpoint `/actuator/health`

Para ver o status de saúde:
```bash
docker compose ps
```

## 🔄 Workflow de Desenvolvimento

1. **Primeira vez**:
```bash
cp .env.example .env
docker compose up -d
```

2. **Após mudanças no código**:
```bash
docker compose build backend
docker compose up -d backend
```

3. **Ver logs**:
```bash
docker compose logs -f backend
```

4. **Ao finalizar**:
```bash
docker compose down
```

## 📝 Notas Importantes

- Os dados do PostgreSQL são persistidos no volume `desafio-postgres-data`
- Os logs do backend são salvos no volume `desafio-backend-logs`
- O PgAdmin só inicia se você usar o profile `dev` ou `tools`
- O backend aguarda o PostgreSQL estar saudável antes de iniciar
- Migrations do Flyway rodam automaticamente ao iniciar

## 🎯 Próximos Passos

Após ter o Docker funcionando, você pode:
1. Configurar CI/CD no GitHub Actions
2. Implementar deploy no Azure
3. Adicionar monitoramento com Prometheus/Grafana
4. Configurar backup automático do banco de dados
