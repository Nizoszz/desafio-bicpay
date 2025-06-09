# 💸 BicPay - Sistema de Transferência de Carteiras (Wallet)

Este projeto é uma solução para o desafio técnico de simular transferências financeiras entre carteiras (wallets), inspirado no modelo do [Picpay](https://github.com/PicPay/picpay-desafio-backend).

A aplicação foi desenvolvida utilizando Java com Spring Boot, banco de dados SQL, e integração com serviços AWS simulados via LocalStack. A arquitetura visa robustez, disponibilidade e separação clara.


---

## 🚀 Tecnologias Utilizadas

### 🧱 Backend
- **Java 17+**
- **Spring Boot**
  - Web (REST APIs)
  - Data JDBC (acesso ao banco)
  - Validation (validação de entrada)
  - Devtools (hot reload)
- **H2**: persistência de dados
- **JUnit / Spring Test**: testes automatizados
- **AWS Spring Cloud SQS**: envio de mensagens para fila SQS

### 🐳 Ambiente Local
- **LocalStack**: simula serviços AWS (SQS)
- **Docker**: para orquestração do LocalStack

---

## 🗂️ Estrutura do Projeto

```
📦src
 ┣ 📂application
 ┃ ┣ 📂gateway
 ┃ ┃ ┣ 📜AuthorizeTransactionGateway
 ┃ ┃ ┣ 📜NotifyTransactionGateway
 ┃ ┣ 📂repository
 ┃ ┃ ┣ 📜TransactionRepository
 ┃ ┃ ┣ 📜WalletRepository
 ┃ ┣ 📂usecase
 ┃ ┃ ┣ 📜GetWallet
 ┃ ┃ ┣ 📜Transfer
 ┣ 📂domain
 ┃ ┣ 📂exception
 ┃ ┃ ┣ 📜UnauthorizedException
 ┃ ┣ 📜Transfer             
 ┃ ┗ 📜GetWallet   
 ┣ 📂infra  
 ┃ ┣ 📂config
 ┃ ┃ ┣ 📜AppConfig
 ┃ ┃ ┣ 📜SQSConfig
 ┃ ┣ 📂queue
 ┃ ┃ ┣ 📜NotificationConsumer
 ┃ ┃ ┣ 📜NotificationProducer
 ┃ ┣ 📂gateway
 ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┣ 📜NotificationProcessingException
 ┃ ┃ ┣ 📜AuthorizeTransactionGatewayImpl
 ┃ ┃ ┣ 📜NotifyTransactionGatewayImpl
 ┃ ┣ 📂rest
 ┃ ┃ ┣ 📂dto
 ┃ ┃ ┣ 📜TransferController
```

---

## 🔄 Regras de Negócio

- **Transferência só pode ser iniciada por usuários com tipo "common"** (usuários "lojistas" não podem transferir).
- A transação só ocorre após autorização de um serviço externo.
- Ao final da transação, uma **notificação** é enviada para uma fila (SQS).
- A operação de transferência é **transacional** — rollback em caso de falha.
- Saldo é **verificado** antes da transferência.

---

## 💻 Executando Localmente

### Pré-requisitos

- Docker
- Java 17+
- H2 DB
- Maven

### 1. Subir o LocalStack
```bash
docker-compose up -d
```

### 2. Configurar o Database
Crie um banco chamado `wallet` e configure no `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    username: sa
    password: 
```

### 3. Rodar a aplicação
```bash
./mvnw spring-boot:run
```

---

## 📑 Exemplo de Requisição

### POST `/api/transfer`

```json
{
  "payer": "c9d3...",
  "payee": "fa2b...",
  "value": 100.00
}
```

📬 Se autorizado, o valor será transferido e uma notificação será enviada para a fila SQS.

---

## 🐳 docker-compose.yml

```yaml
version: '3.8'

services:
  localstack:
    image: localstack/localstack:latest
    container_name: localstack
    ports:
      - "4566:4566"
    environment:
      - SERVICES=sqs
      - DEBUG=1
      - DATA_DIR=/tmp/localstack/data
    volumes:
      - "./localstack:/tmp/localstack"
      - "/var/run/docker.sock:/var/run/docker.sock"
```

---

## ✅ Sobre a Disponibilidade com SQS

A **fila SQS** é utilizada para garantir que a **notificação de transação não bloqueie** a operação principal. Com isso, mesmo que o sistema de notificação esteja fora do ar, a transação pode ser concluída, e a entrega da notificação será tentada posteriormente.

---

## 🧪 Testes

- Banco de dados isolado com H2 para testes de integração.
- Cobertura para os casos de uso e regras de negócio.

---

## 📌 Considerações

Este projeto demonstra práticas como:
- Design por casos de uso (Application Service Layer)
- Resiliência com integração externa
- Arquitetura limpa com separação entre domínio, infraestrutura e gateways
- Dockerização para serviços simulados AWS

---

## 🧑‍💻 Autor

Desenvolvido por Andrew da Silva  
[LinkedIn](https://linkedin.com/in/andrewairamdasilva) • [GitHub](https://github.com/Nizoszz)
