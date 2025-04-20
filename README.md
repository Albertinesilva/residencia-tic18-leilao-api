<h1 align="center">🕵️‍♂️ Leilão Secreto Online - API Backend</h1>

📚 Contexto do Projeto

Este projeto foi desenvolvido como parte da **avaliação técnica** da disciplina de **Java Backend** da **Residência em Software**. O objetivo foi aplicar conhecimentos práticos na construção de uma API RESTful com regras de negócio específicas simulando um sistema real de leilão secreto.

---

🎯 Objetivo da Avaliação

Criar uma aplicação **Java + Spring Boot** que simule um sistema de **leilão secreto online**, contendo regras de negócio definidas, endpoints REST bem estruturados e organização limpa do código.

O sistema deve:

- Cadastrar e gerenciar **leilões**;
- Cadastrar **concorrentes**;
- Registrar **lances secretos**, ou seja, sem visibilidade dos concorrentes;
- Determinar o **vencedor** do leilão com base no maior lance.

---

🗃️ Banco de Dados
Banco: H2 Database

Configuração: Localizado em /leilao/src/main/resources/application.properties

📌 Observação: Status do Leilão

O **status de um leilão** (se está **ABERTO** ou **FECHADO**) é determinado pelo campo `leilaoStatus` da entidade `Leilao`.

🧠 Esse campo é mapeado para o enum LeilaoStatus:

```java
/**
 * Status do leilão.
 * Este campo armazena o código do status do leilão, que é mapeado para um valor do enum {@link LeilaoStatus}.
 * O status pode ser "ABERTO" (código "1") ou "FECHADO" (código "2").
 */
@JsonDeserialize(using = LeilaoStatusDeserializer.class)
public enum LeilaoStatus {
  ABERTO("1"),   // Leilão aberto para lances
  FECHADO("2");  // Leilão fechado, sem mais possibilidade de lances
}
```

🔄 A desserialização é feita por LeilaoStatusDeserializer, que converte o código "1" em ABERTO e "2" em FECHADO.

⚠️ Regras aplicadas com base nesse status:

- Lances só são permitidos quando leilaoStatus é "1" (ABERTO).

- Se leilaoStatus for "2" (FECHADO), os endpoints de /lance (POST, PUT, DELETE) retornam:

  - 403 Forbidden para criação ou alteração de lances.

  - 403 Forbidden para exclusão de lances também.

---

🧩 Diagrama UML

📌 Imagem abaixo representa o modelo de entidades do sistema:

![Diagrama UML do Leilão Secreto](leilao-secreto.png)

---

🌐 Endpoints da API

> Todas as requisições e respostas são em formato `JSON`.

### 🔹 Leilão `/leilao/`

| Método | Rota           | Descrição                    |
| ------ | -------------- | ---------------------------- |
| GET    | `/leilao/`     | Lista todos os leilões       |
| GET    | `/leilao/{id}` | Retorna um leilão específico |
| POST   | `/leilao/`     | Cria um novo leilão          |
| PUT    | `/leilao/{id}` | Atualiza um leilão existente |
| DELETE | `/leilao/{id}` | Remove um leilão             |

🔹 Endpoint /leilao/

**🔍 GET**

`/leilao/{id}`

  📥 Retorna o DTO do leilão indicado.

❌ `404` se o ID for inválido.

`/leilao/`

📥 Retorna todos os leilões cadastrados.

✅ `200` em caso de sucesso.

**➕ POST**

📤 Envia um DTO sem ID para criar um novo leilão.

✅ `201` (Created) em sucesso.

❌ `400` em erro.

**♻️ PUT**

`/leilao/{id}`

📤 Atualiza um leilão existente com base no ID.

✅ `200` com DTO atualizado.

❌ `404` se ID for inválido ou ausente.

**🗑️ DELETE**

`/leilao/{id}`

🧹 Remove um leilão.

✅ `200` se bem-sucedido.

❌ `404` se ID for inválido ou ausente.

---

### 🔹 Concorrente `/concorrente/`

| Método | Rota                | Descrição                         |
| ------ | ------------------- | --------------------------------- |
| GET    | `/concorrente/`     | Lista todos os concorrentes       |
| GET    | `/concorrente/{id}` | Retorna um concorrente específico |
| POST   | `/concorrente/`     | Cadastra um novo concorrente      |
| PUT    | `/concorrente/{id}` | Atualiza dados de um concorrente  |
| DELETE | `/concorrente/{id}` | Remove um concorrente             |

🔹 Endpoint /concorrente/

🔍 GET

`/concorrente/{id}`

📥 Retorna apenas o nome do concorrente.

❌ `404` se o ID for inválido.

`/concorrente/`

📥 Retorna todos os concorrentes.

✅ `200` em caso de sucesso.

**➕ POST**

📤 Cria um novo concorrente com DTO sem ID.

✅ `201` (Created).

❌ `400` em erro.

**♻️ PUT**

`/concorrente/{id}`

📤 Atualiza concorrente por ID.

✅ `200` com DTO atualizado.

❌ `404` se ID for inválido ou ausente.

**🗑️ DELETE**

`/concorrente/{id}`

🧹 Remove um concorrente.

✅ `200` se sucesso.

❌ `404` se ID for inválido ou ausente.

---

### 🔹 Lance `/lance/`

| Método | Rota                      | Descrição                         |
| ------ | ------------------------- | --------------------------------- |
| GET    | `/lance/`                 | Lista todos os lances             |
| GET    | `/lance/{id}`             | Detalha um lance específico       |
| GET    | `/lance/leilao={id}`      | Lista os lances de um leilão      |
| GET    | `/lance/concorrente={id}` | Lista os lances de um concorrente |
| POST   | `/lance/`                 | Registra um novo lance            |
| PUT    | `/lance/{id}`             | Atualiza um lance                 |
| DELETE | `/lance/{id}`             | Remove um lance                   |

🔹 Endpoint /lance/

**🔍 GET**

`/lance/{id}`

📥 Retorna DTO com ID do leilão, concorrente e valor do lance.

❌ `404` se o ID for inválido.

`/lance/`

📥 Retorna todos os lances.

📌 Com filtros:

`/lance/leilao={id}` → Lances por leilão.

`/lance/concorrente={id}` → Lances por concorrente.

✅ `200` ou ❌ `404`.

**➕ POST**

📤 Cria lance com IDs de leilão, concorrente e valor (sem ID).

✅ `201` (Created).

❌ `403` se concorrente não existir.

❌ `400` se leilão não existir.

❌ `403` se leilão estiver fechado.

**♻️ PUT**

`/lance/{id}`

📤 Atualiza lance por ID.

✅ `200` com DTO atualizado.

❌ `404` se ID inválido ou ausente.

❌ `403` se concorrente não existir.

❌ `400` se leilão não existir.

❌ `403` se leilão estiver fechado.

**🗑️ DELETE**

`/lance/{id}`

🧹 Remove um lance.

✅ `200` se sucesso.

❌ `404` se ID inválido ou ausente.

❌ `403` se o leilão do lance estiver fechado.

📌 **Regra de negócio importante:**

- Um lance só pode ser registrado se:
  - O **concorrente** existir;
  - O **leilão** existir e estiver com status `ABERTO`;
- Caso contrário, a API retorna erros `403 Forbidden` ou `400 Bad Request`.

---

### 🔹 Resultado `/vencedor_leilao/{id}`

| Método | Rota                    | Descrição                                  |
| ------ | ----------------------- | ------------------------------------------ |
| GET    | `/vencedor_leilao/{id}` | Retorna o vencedor do leilão (maior lance) |

🏆 Endpoint `/vencedor_leilao/{id}`

**🔍 GET**

📥 Retorna:

- Dados do leilão
- Valor do maior lance
- O **concorrente vencedor** e o valor do lance.
- Apenas para leilões encerrados.

- `DTO` do concorrente vencedor

❌ `404` se ID for inválido

❌ `400` se ID não for passado

❌ `403` se leilão estiver fechado

---

**Conclusão**

Neste projeto, foi possível aplicar com sucesso os conhecimentos adquiridos em **Java Backend** e **Spring Boot** na construção de uma API RESTful para um **leilão secreto online**. A implementação de regras de negócio, como o controle do status de leilão (ABERTO/FECHADO) e a validação de lances, foi realizada com o uso de **enumerações** e **desserialização customizada**. A integração com o **banco de dados H2** permitiu o armazenamento eficiente dos dados, enquanto a estruturação e documentação dos endpoints da API seguiram boas práticas, proporcionando uma experiência prática e profunda na criação de sistemas **RESTful**. Este projeto consolidou o aprendizado em desenvolvimento de APIs, reforçando a compreensão dos conceitos de design de API, segurança e boas práticas no backend.

---

📬 **Contato e redes:**

| 🌎 **LinkedIn**                                                              | 📧 **E-mail**                                                     |
| ---------------------------------------------------------------------------- | ----------------------------------------------------------------- |
| [Albert Silva](https://www.linkedin.com/in/albert-backend-java-spring-boot/) | [albertinesilva.17@gmail.com](mailto:albertinesilva.17@gmail.com) |
