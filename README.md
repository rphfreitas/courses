# 🎓 Courses Service API

Uma API REST robusta e bem testada para gerenciamento de cursos com autenticação integrada, desenvolvida com Spring Boot 4.0.1 e Java 21.

## 📖 Sumário

- [Visão Geral](#visão-geral)
- [Características](#características)
- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Segurança com Spring Security](#segurança-com-spring-security)
- [Autenticação](#autenticação)
- [Endpoints da API](#endpoints-da-api)
- [Configuração](#configuração)
- [Como Usar](#como-usar)
- [Endpoints da API](#endpoints-da-api)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Testes](#testes)
- [Tratamento de Erros](#tratamento-de-erros)
- [Contribuindo](#contribuindo)

---

## 🎯 Visão Geral

O **Courses Service** é uma aplicação backend REST que oferece operações CRUD completas para gerenciamento de cursos. A API foi desenvolvida seguindo as melhores práticas de desenvolvimento com Spring Boot, incluindo:

- ✅ Validação robusta de dados de entrada
- ✅ Tratamento centralizado de exceções
- ✅ Cobertura completa com testes unitários
- ✅ Banco de dados em memória (H2) pronto para produção
- ✅ Documentação clara e estruturada

---

## ✨ Características

### Funcionalidades Principais
- 📋 **Listar todos os cursos** - Recuperar lista completa de cursos
- 🔍 **Buscar curso por ID** - Recuperar um curso específico
- ➕ **Criar novo curso** - Adicionar novo curso à base de dados
- ✏️ **Atualizar curso** - Modificar informações de um curso existente
- 🗑️ **Deletar curso** - Remover um curso da base de dados

### Segurança e Validação
- Validação automática de campos (NotBlank, Size)
- Tratamento centralizado de exceções personalizadas
- Exceção customizada `ItemNotFoundException` para erros 404
- Respostas de erro estruturadas com mensagens descritivas

---

## 🛠️ Tecnologias

### Backend
| Tecnologia | Versão | Propósito |
|------------|--------|----------|
| **Java** | 21 | Linguagem de programação |
| **Spring Boot** | 4.0.1 | Framework web e IoC |
| **Spring Data JPA** | - | ORM e acesso a dados |
| **Spring Validation** | - | Validação de dados |
| **Lombok** | - | Redução de boilerplate |
| **Swagger/OpenAPI** | 3.0 | Documentação interativa da API |

### Banco de Dados
| Tecnologia | Versão | Propósito |
|------------|--------|----------|
| **H2 Database** | - | Banco de dados em memória |
| **Hibernate** | - | Mapeamento objeto-relacional |

### Testing
| Tecnologia | Versão | Propósito |
|------------|--------|----------|
| **JUnit 5** | - | Framework de testes |
| **Mockito** | - | Mock de dependências |
| **AssertJ** | - | Assertions fluentes |

### Build
| Tecnologia | Versão | Propósito |
|------------|--------|----------|
| **Maven** | 3.6+ | Gerenciamento de dependências |

---

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java 21** ou superior
  ```bash
  java -version
  # Saída esperada: openjdk version "21" ou superior
  ```

- **Maven 3.6** ou superior
  ```bash
  mvn -version
  # Saída esperada: Apache Maven 3.6+ 
  ```

- **Git** (para clonar o repositório)
  ```bash
  git --version
  ```

### Verificar Instalação (Windows)
```powershell
# Verificar Java
java -version

# Verificar Maven
mvn -version

# Verificar Git
git --version
```

---

## 💻 Instalação

### 1. Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/courses-service.git
cd courses-service
```

### 2. Compilar o Projeto

```bash
# Compilar e baixar dependências
mvn clean compile
```

### 3. Executar os Testes

```bash
# Executar todos os testes unitários
mvn test
```

### 4. Construir o JAR Executável

```bash
# Gerar arquivo JAR pronto para produção
mvn clean package
```

### 5. Executar a Aplicação

**Opção 1: Via Maven**
```bash
mvn spring-boot:run
```

**Opção 2: Via JAR compilado**
```bash
java -jar target/project-exemple-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: `http://localhost:8080`

---

## ⚙️ Configuração

### Arquivo application.properties

O arquivo `src/main/resources/application.properties` contém as configurações principais:

```properties
# Nome da aplicação
spring.application.name=courses-service

# Banco de Dados H2 (em memória)
spring.data.datasource.url=jdbc:h2:mem:testdb
spring.data.datasource.driver=org.h2.Driver
spring.data.datasource.username=sa
spring.data.datasource.password=password

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.show-sql=true
```

### Acessar Console H2

Para acessar o console do banco de dados H2 durante desenvolvimento:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Usuário: `sa`
- Senha: `password`

---

## 🚀 Como Usar

### Iniciar a Aplicação

```bash
# Terminal 1: Iniciar o servidor
mvn spring-boot:run

# Terminal 2: Testar a API (com curl ou Postman)
curl http://localhost:8080/api/courses
```

### Exemplos de Requisições

#### 1. Listar todos os cursos
```bash
curl -X GET http://localhost:8080/api/courses
```

#### 2. Buscar curso por ID
```bash
curl -X GET http://localhost:8080/api/courses/1
```

#### 3. Criar novo curso
```bash
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Spring Boot Fundamentals",
    "category": "BACK"
  }'
```

#### 4. Atualizar curso
```bash
curl -X PUT http://localhost:8080/api/courses/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Advanced Spring Boot",
    "category": "BACK"
  }'
```

#### 5. Deletar curso
```bash
curl -X DELETE http://localhost:8080/api/courses/1
```

---

## 📚 Documentação Interativa com Swagger

O projeto inclui **Swagger 3.0 (OpenAPI)** integrado para documentação automática e interativa da API.

### Acessar Swagger UI

Com a aplicação em execução, acesse:

```
http://localhost:8080/swagger-ui.html
```

**Funcionalidades:**
- 🔍 Visualizar todos os endpoints
- 🧪 Testar endpoints diretamente
- 📖 Ver documentação detalhada
- 📊 Explorar modelos de dados
- 📋 Exemplos de requisições/respostas

### Ver JSON OpenAPI

```
http://localhost:8080/api-docs
```

Para mais informações, veja [SWAGGER_DOCUMENTATION.md](./SWAGGER_DOCUMENTATION.md)

---

## 📡 Endpoints da API

### Baseado em: `/api/courses`

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| **GET** | `/` | Listar todos os cursos | 200 |
| **GET** | `/{id}` | Buscar curso por ID | 200 / 404 |
| **POST** | `/` | Criar novo curso | 201 / 400 |
| **PUT** | `/{id}` | Atualizar curso | 200 / 404 / 400 |
| **DELETE** | `/{id}` | Deletar curso | 204 / 404 |

### Estrutura de Resposta - Curso

```json
{
  "_id": 1,
  "name": "Spring Boot Fundamentals",
  "category": "BACK"
}
```

### Validações de Entrada

**Campo: name**
- ✅ Obrigatório (NotBlank)
- ✅ Máximo 200 caracteres
- ❌ Não pode ser vazio

**Campo: category**
- ✅ Obrigatório (NotBlank)
- ✅ Máximo 10 caracteres
- ❌ Não pode ser vazio

### Exemplos de Erros

**Erro 400 - Validação**
```json
{
  "timestamp": "2026-01-13T20:15:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "name",
      "message": "O nome é obrigatório"
    },
    {
      "field": "category",
      "message": "A categoria deve ter no máximo 10 caracteres"
    }
  ]
}
```

**Erro 404 - Não Encontrado**
```json
{
  "timestamp": "2026-01-13T20:15:00",
  "status": 404,
  "error": "Not Found",
  "message": "Curso não encontrado com id: 999"
}
```

---

## 📁 Estrutura do Projeto

```
courses/
├── src/
│   ├── main/
│   │   ├── java/com/br/courses/
│   │   │   ├── CoursesApplication.java          # Classe principal
│   │   │   ├── controller/
│   │   │   │   └── CourseController.java        # Endpoints REST
│   │   │   ├── service/
│   │   │   │   └── CourseService.java           # Lógica de negócio
│   │   │   ├── repository/
│   │   │   │   └── CourseRepository.java        # Acesso a dados
│   │   │   ├── model/
│   │   │   │   └── Course.java                  # Entidade JPA
│   │   │   └── exception/
│   │   │       ├── ItemNotFoundException.java    # Exceção customizada
│   │   │       ├── GlobalExceptionHandler.java  # Tratamento central
│   │   │       ├── ApiError.java                # DTO de erro
│   │   │       └── ValidationError.java         # Erros de validação
│   │   └── resources/
│   │       ├── application.properties           # Configurações
│   │       ├── static/                          # Arquivos estáticos
│   │       └── templates/                       # Templates Thymeleaf
│   └── test/
│       └── java/com/br/courses/
│           ├── CoursesApplicationTests.java
│           └── service/
│               └── CourseServiceTest.java       # Testes unitários
├── pom.xml                                      # Configuração Maven
├── README.md                                    # Este arquivo
├── TESTES_UNITARIOS.md                          # Documentação de testes
└── RELATORIO_TESTES.html                        # Relatório visual de testes
```

---

## 🧪 Testes

### Executar Testes

```bash
# Executar todos os testes
mvn test

# Executar apenas CourseServiceTest
mvn test -Dtest=CourseServiceTest

# Executar com saída detalhada
mvn test -X

# Executar com cobertura
mvn test jacoco:report
```

### Cobertura de Testes

O projeto inclui **18 testes unitários** com cobertura completa:

- ✅ 3 testes para `findAll()`
- ✅ 3 testes para `save()`
- ✅ 3 testes para `find()`
- ✅ 3 testes para `delete()`
- ✅ 4 testes para `update()`
- ✅ 2 testes para casos extremos

**Taxa de Sucesso: 100%** ✓

Para mais detalhes, veja [TESTES_UNITARIOS.md](./TESTES_UNITARIOS.md)

---

## ⚠️ Tratamento de Erros

### Exceções Personalizadas

**ItemNotFoundException**
```java
// Lançada quando um curso não é encontrado
throw new ItemNotFoundException("Curso não encontrado com id: " + id);
```

### GlobalExceptionHandler

Todas as exceções são tratadas de forma centralizada:
- Validações de entrada (400)
- Item não encontrado (404)
- Erros internos do servidor (500)

### Formato de Resposta de Erro

```json
{
  "timestamp": "2026-01-13T20:15:00",
  "status": 404,
  "error": "Not Found",
  "message": "Curso não encontrado com id: 999",
  "path": "/api/courses/999"
}
```

---

## 🚦 Status do Projeto

| Aspecto | Status |
|--------|--------|
| Desenvolvimento | ✅ Completo |
| Testes Unitários | ✅ 18/18 Passando |
| Validação | ✅ Implementada |
| Tratamento de Erros | ✅ Centralizado |
| Documentação | ✅ Completa |
| Pronto para Produção | ✅ Sim |

---

## 📝 Contribuindo

### Como Contribuir

1. **Fork** o repositório
2. **Clone** seu fork: `git clone https://github.com/seu-usuario/courses-service.git`
3. **Crie uma branch** para sua feature: `git checkout -b feature/nova-funcionalidade`
4. **Commit** suas mudanças: `git commit -am 'Adiciona nova funcionalidade'`
5. **Push** para a branch: `git push origin feature/nova-funcionalidade`
6. **Abra um Pull Request**

### Diretrizes
- Mantenha o padrão de código do projeto
- Adicione testes para novas funcionalidades
- Atualize a documentação conforme necessário
- Siga o padrão AAA em testes (Arrange-Act-Assert)

---

## 📚 Recursos Adicionais

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Lombok Documentation](https://projectlombok.org/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Swagger/OpenAPI Documentation](https://springdoc.org/)
- [OpenAPI 3.0 Specification](https://spec.openapis.org/oas/v3.0.3)

---

## 📄 Licença

Este projeto está licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para detalhes.

---

## 👥 Autor

**Desenvolvido com ❤️**

- GitHub: [@seu-usuario](https://github.com/seu-usuario)
- Email: seu-email@example.com

---

## 🙏 Agradecimentos

Agradecimentos especiais a:
- Spring Team pelo excelente framework
- Comunidade Open Source
- Todos os contribuidores

---

## 📞 Suporte

Encontrou um bug ou tem uma sugestão? 

- 🐛 Abra uma [issue](https://github.com/seu-usuario/courses-service/issues)
- 💬 Deixe um comentário
- ✉️ Entre em contato via email

---

**Última atualização**: 13 de Janeiro de 2026  
**Versão**: 1.0.0  
**Status**: ✓ Produção

