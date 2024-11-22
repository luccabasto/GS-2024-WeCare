# WeCare API

### Descrição da Arquitetura

WeCare é um sistema desenvolvido para ajudar usuários a gerenciar hábitos, metas e manter a motivação. A aplicação foi construída usando Java Spring Boot, com persistência de dados via banco de dados e documentação utilizando Swagger. Segue as melhores práticas de arquitetura RESTful e atende ao nível 3 de maturidade proposto por Leonard Richardson.

**Tecnologias Utilizadas**

* JAVA 21
* SPRING BOOT 
* ORACLE SQL / MYSQL
* SWAGGER (OPENAPI3)
* HETEOAS
* MAVEN 
* JUnit 5

**Funcionalidades**

* Gerenciamento de Usuários

    * Criar, atualizar, listar e excluir usuários.
    * Validação de dados com Bean Validation.

* Gerenciamento de Metas

    * CRUD de metas associadas a usuários.
    * Relacionamento entre tabelas com Spring Data JPA.

* Gerenciamento de Hábitos

    * CRUD de hábitos associados a usuários.
    * Paginação e suporte a múltiplos registros.

* Documentação e Testes

    * Documentação com Swagger.
    * Testes automatizados usando JUnit.

<hr>

**Swagger UI** 


**Test via Postman**

1. CRUD de Usuários
_Criar Usuário (POST /api/users)_

Exemplo JSON:

{
  "nome": "John Doe",
  "email": "johndoe@example.com",
  "idade": 25,
  "motivacao": "Stay fit and healthy",
  "endereco": "Rua Verde, 123"
}

_Obter todos os Usuários (GET/api/users)_

Resposta JSON:

[
  {
    "id": 1,
    "name": "John Doe",
    "email": "johndoe@example.com",
    "age": 25,
    "motivation": "Stay fit and healthy",
    "address": "123 Main Street",
    "_links": {
      "self": {"href": "http://localhost:8080/api/users/1"},
      "all-users": {"href": "http://localhost:8080/api/users"}
    }
  }
]

2. CRUD de Metas
_Criar Meta (POST/api/goals)_

Exemplo JSON:

{
  "title": "Lose Weight",
  "description": "Lose 5 kg in 3 months",
  "userId": 1
}

_Obter Todas as Metas (GET/api/goals)_

Resposta JSON:

[
  {
    "id": 1,
    "title": "Lose Weight",
    "description": "Lose 5 kg in 3 months",
    "userId": 1
  }
]

3. CRUD de Hábitos

_Criar Hábito (POST/api/habits)_

Exemplo JSON:

{
  "name": "Morning Run",
  "userId": 1
}

_Obter Todos os Hábitos (GET/api/habits)_

Resposta JSON:

[
  {
    "id": 1,
    "name": "Morning Run",
    "userId": 1
  }
]

### Credenciais para Testes

* Usuário para acesso à API:
    * URL do banco: jdbc:mysql://cloud.mysql.com/wecare

    * Usuário: test_user
    * Senha: password123

* API KEY (Caso seja necessário):

    API-KEY: abc123-def456-gh789



-------------------------------------------------

WeCare - solução desenvolvida para GS 2024

Lucas Basto - RM553771
Erick Lopes - RM553927
Gabriel Bragança - RM554064


[LINK for Github(https://github.com/luccabasto/GS-2024-WeCare)]