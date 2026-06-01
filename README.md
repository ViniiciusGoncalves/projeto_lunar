# 🌕 Lunar Base Control API

API REST desenvolvida em Java 17 com Spring Boot para controle e monitoramento dos recursos de uma base lunar. Permite o gerenciamento completo de sensores, reservatórios, consumo de energia, sistemas de climatização e alertas operacionais.

---

## 👥 Integrantes

| Nome | RM |
|------|----|
|Vinicius Gonçalves      |561784   |




---

## 📋 Descrição do Projeto

A **Lunar Base Control API** simula o sistema de controle de uma base lunar habitada, oferecendo endpoints para:

- **Sensores** — temperatura, umidade, luminosidade, nível de água, pressão atmosférica, qualidade do ar, radiação solar, consumo de energia e nível de bateria
- **Reservatórios** — controle de nível, tipo de líquido e alertas de abastecimento
- **Consumo de Energia** — monitoramento por setor, geração solar e balanço energético
- **Climatização & Atuadores** — ventilação, iluminação, bombas de água, válvulas e painéis solares
- **Alertas Operacionais** — geração automática de alertas ao detectar leituras fora dos limites, com suporte a notificações visuais e sonoras
- **Dashboard** — visão geral consolidada da base em uma única requisição

---

## 🏗️ Arquitetura

```
src/main/java/com/lunarbase/
├── controller/        # Recebimento e resposta às requisições HTTP
├── service/           # Regras de negócio e lógica de operação
├── repository/        # Acesso e persistência dos dados (Spring Data JPA)
├── model/             # Entidades e enums do domínio
├── dto/               # Objetos de transferência de dados
├── exception/         # Tratamento global de erros
└── config/            # CORS, inicialização de dados de exemplo
```

---

## 🛠️ Tecnologias

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Java | 17 | Linguagem principal |
| Spring Boot | 3.2.5 | Framework base |
| Spring Data JPA | — | Persistência de dados |
| Spring Validation | — | Validação de campos |
| H2 Database | — | Banco de dados em modo file |
| Lombok | — | Redução de boilerplate |
| Maven | — | Gerenciamento de dependências |

---

## ⚙️ Configuração e Execução

### Pré-requisitos

- Java 17+
- Maven 3.8+

### Clonando o repositório

```bash
git clone https://github.com/<seu-usuario>/lunar-base-api.git
cd lunar-base-api
```

### Executando a aplicação

```bash
# Com Maven Wrapper
./mvnw spring-boot:run

# Ou com Maven instalado
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8081`

### Banco de dados H2 (modo persistente)

Os dados são armazenados em arquivo na pasta `./data/lunarbasedb`. O console web do H2 pode ser acessado em:

```
URL:      http://localhost:8081/h2-console
JDBC URL: jdbc:h2:file:./data/lunarbasedb
Usuário:  lunarbase
Senha:    lunar2024
```

> Ao iniciar pela primeira vez, o sistema popula automaticamente o banco com dados de exemplo (sensores, reservatórios, registros de energia, sistemas e alertas).

---

## 🚀 Endpoints

Todas as respostas seguem o padrão:

```json
{
  "sucesso": true,
  "mensagem": "Descrição da operação",
  "dados": { },
  "timestamp": "2025-01-01T12:00:00"
}
```

### 🌡️ Sensores — `/api/sensores`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/sensores` | Cadastra novo sensor |
| `GET` | `/api/sensores` | Lista todos (filtros: `?tipo=` `?status=` `?localizacao=`) |
| `GET` | `/api/sensores/{id}` | Busca sensor por ID |
| `GET` | `/api/sensores/alertas` | Lista sensores com leitura fora dos limites |
| `PUT` | `/api/sensores/{id}` | Atualiza dados do sensor |
| `PATCH` | `/api/sensores/{id}/leitura` | Registra nova leitura (gera alerta automático se fora do limite) |
| `DELETE` | `/api/sensores/{id}` | Remove sensor |

**Tipos de sensor disponíveis:**
`TEMPERATURA` `UMIDADE` `LUMINOSIDADE` `NIVEL_AGUA` `PRESSAO_ATMOSFERICA` `QUALIDADE_AR` `RADIACAO_SOLAR` `CONSUMO_ENERGIA` `VELOCIDADE_VENTO` `NIVEL_BATERIA`

**Exemplo — cadastrar sensor:**
```json
POST /api/sensores
{
  "nome": "Termômetro Módulo A",
  "tipo": "TEMPERATURA",
  "localizacao": "Módulo Habitacional A",
  "unidadeMedida": "°C",
  "limiteMinimo": 15.0,
  "limiteMaximo": 35.0
}
```

**Exemplo — registrar leitura:**
```json
PATCH /api/sensores/1/leitura
{
  "valorLeitura": 41.5
}
```

---

### 💧 Reservatórios — `/api/reservatorios`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/reservatorios` | Cadastra novo reservatório |
| `GET` | `/api/reservatorios` | Lista todos (filtro: `?status=`) |
| `GET` | `/api/reservatorios/{id}` | Busca por ID (inclui `nivelPercentual` calculado) |
| `GET` | `/api/reservatorios/nivel-baixo` | Lista reservatórios abaixo do limite mínimo |
| `PUT` | `/api/reservatorios/{id}` | Atualiza configurações |
| `PATCH` | `/api/reservatorios/{id}/nivel` | Atualiza nível atual do líquido |
| `DELETE` | `/api/reservatorios/{id}` | Remove reservatório |

**Tipos de líquido:** `AGUA_POTAVEL` `AGUA_RESIDUAL` `COMBUSTIVEL` `OXIGENIO_LIQUIDO` `AGUA_DESTILADA`

**Exemplo — atualizar nível:**
```json
PATCH /api/reservatorios/1/nivel
{
  "nivelAtual": 42000.0
}
```

---

### ⚡ Energia — `/api/energia`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/energia` | Cadastra registro de consumo de um setor |
| `GET` | `/api/energia` | Lista consumo de todos os setores |
| `GET` | `/api/energia/{id}` | Busca por ID (inclui `balancoEnergetico` calculado) |
| `GET` | `/api/energia/resumo` | Resumo global (consumo total, geração total, balanço) |
| `GET` | `/api/energia/alerta` | Setores com consumo acima do limite configurado |
| `PUT` | `/api/energia/{id}` | Atualiza dados de consumo |
| `DELETE` | `/api/energia/{id}` | Remove registro |

---

### 🌬️ Climatização — `/api/climatizacao`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/climatizacao` | Cadastra sistema (ventilação, iluminação, bomba, válvula) |
| `GET` | `/api/climatizacao` | Lista todos (filtros: `?tipo=` `?status=`) |
| `GET` | `/api/climatizacao/{id}` | Busca por ID |
| `PUT` | `/api/climatizacao/{id}` | Atualiza configurações |
| `PATCH` | `/api/climatizacao/{id}/acionar` | Liga ou desliga o sistema remotamente |
| `PATCH` | `/api/climatizacao/{id}/intensidade` | Ajusta intensidade (0–100%) |
| `DELETE` | `/api/climatizacao/{id}` | Remove sistema |

**Tipos de sistema:** `VENTILACAO` `ILUMINACAO` `BOMBA_AGUA` `VALVULA_AGUA` `AR_CONDICIONADO` `EXAUSTOR` `PAINEL_SOLAR`

**Exemplo — acionar sistema:**
```json
PATCH /api/climatizacao/1/acionar
{
  "ligar": true,
  "intensidade": 75
}
```

---

### 🚨 Alertas — `/api/alertas`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/alertas` | Cria alerta manual |
| `GET` | `/api/alertas` | Lista todos (filtros: `?status=` `?severidade=` `?tipo=` `?setor=`) |
| `GET` | `/api/alertas/ativos` | Lista alertas ativos ordenados por severidade |
| `GET` | `/api/alertas/{id}` | Busca por ID |
| `PUT` | `/api/alertas/{id}` | Atualiza alerta |
| `PATCH` | `/api/alertas/{id}/reconhecer` | Operador reconhece o alerta |
| `PATCH` | `/api/alertas/{id}/resolver` | Marca alerta como resolvido |
| `DELETE` | `/api/alertas/{id}` | Remove alerta |

**Ciclo de vida do alerta:** `ATIVO` → `RECONHECIDO` → `RESOLVIDO`

**Severidades:** `INFO` `AVISO` `CRITICO` `EMERGENCIA`

**Notificações:** `VISUAL` `SONORO` `VISUAL_E_SONORO` `PUSH_MOBILE` `TODOS`

> Alertas são gerados **automaticamente** quando `PATCH /sensores/{id}/leitura` detecta leitura fora dos limites configurados.

**Exemplo — reconhecer alerta:**
```json
PATCH /api/alertas/1/reconhecer
{
  "operador": "João Silva"
}
```

---

### 📊 Dashboard — `/api/dashboard`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/dashboard` | Resumo completo da base em uma única requisição |

**Retorna:**
```json
{
  "totalSensores": 10,
  "sensoresAtivos": 10,
  "sensoresFalha": 0,
  "sensoresAlerta": 0,
  "totalReservatorios": 4,
  "reservatoriosNivelBaixo": 1,
  "totalAguaLitros": 42700.0,
  "percentualMedioAgua": 76.3,
  "consumoTotalKwh": 71.4,
  "geracaoTotalKwh": 48.0,
  "balancoEnergeticoKwh": -23.4,
  "setoresBateriaCritica": 1,
  "sistemasLigados": 5,
  "sistemasEmFalha": 0,
  "consumoClimatizacaoWatts": 1150.0,
  "alertasAtivos": 2,
  "alertasCriticos": 1,
  "alertasEmergencia": 0,
  "geradoEm": "2025-01-01T12:00:00"
}
```

---

## 🔗 Integração com App Mobile

A API foi projetada para integração com o aplicativo mobile desenvolvido na disciplina de **Advanced Programming And Mobile Dev**:

- **CORS configurado** para aceitar requisições de qualquer origem (`*`)
- **Respostas padronizadas** com envelope `ApiResponse<T>` facilitando o parse no mobile
- Campos `criadoEm` e `atualizadoEm` em todas as entidades para sincronização
- Suporte a `PATCH` granular (atualizar só a leitura, só o nível, só a intensidade) para economizar banda
- Banco H2 em **file mode** — dados persistem entre reinicializações do servidor

---

## 📁 Estrutura do Projeto

```
lunar-base-api/
├── src/
│   └── main/
│       ├── java/com/lunarbase/
│       │   ├── LunarBaseApiApplication.java
│       │   ├── config/
│       │   │   ├── CorsConfig.java
│       │   │   └── DataInitializer.java
│       │   ├── controller/
│       │   │   ├── SensorController.java
│       │   │   ├── ReservatorioController.java
│       │   │   ├── ConsumoEnergiaController.java
│       │   │   ├── ClimatizacaoController.java
│       │   │   ├── AlertaOperacionalController.java
│       │   │   └── DashboardController.java
│       │   ├── service/
│       │   ├── repository/
│       │   ├── model/
│       │   ├── dto/
│       │   └── exception/
│       └── resources/
│           └── application.properties
├── data/                  # Gerado na primeira execução (banco H2)
├── pom.xml
└── README.md
```

---

## 📄 Licença

Projeto acadêmico — FIAP. Todos os direitos reservados aos integrantes do grupo.
