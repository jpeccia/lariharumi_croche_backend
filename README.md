<h1 align="center" style="font-weight: bold;">Leveling Life 💻</h1>

<p align="center">
  <a href="#functions">Funcionalidades</a> •
 <a href="#technologies">Tecnologias Utilizadas</a> • 
 <a href="#started">Configuração e Execução</a> • 
 <a href="#contribute">Contribua</a>
</p>

<p align="center">
    <b>Esta API é um sistema completo de gerenciamento de produtos e categorias com autenticação de usuário(admin), com foco em segurança e controle de acesso. Ela oferece uma variedade de funcionalidades, incluindo o registro, login e a autenticação, bem como o gerenciamento de produtos em um catálogo. A API utiliza autenticação baseada em tokens JWT para garantir a segurança e o acesso restrito às rotas protegidas.
</b>
</p>

<h2 id="functions">🚀 Funcionalidades</h2>

- **Autenticação e Registro** 🔒: Segurança com autenticação JWT para proteger o acesso de usuários.
- **Catalogo com Produtos e suas Categorias** : Os usuários podem acessar os produtos e suas categorias
- **Sistema de Administrador para Adicionar Produtos E Categoria** : Sistema para o usuário admin conseguir ter um controle de produtos e categorias.
- **Documentação com Swagger** 📖: A API é totalmente documentada com Swagger.


<h2 id="technologies">🛠️ Tecnologias Utilizadas</h2>

- **Spring Boot** 🌱: Estrutura robusta para o desenvolvimento da API RESTful.
- **JWT com Auth0** 🔑: Autenticação segura para gerenciamento de sessões de usuários.
- **Swagger** 📜: Documentação automática para as endpoints.
- **Docker** 🐳: Containers para isolar o ambiente e facilitar o desenvolvimento e a implantação.
- **Insomnia** 🌐: Ferramenta de teste para endpoints durante o desenvolvimento.
- **PostgreSQL** 🗄️: Banco de dados relacional utilizado para persistir os dados da aplicação.
- **JUnit e Mockito** 🧪: Ferramentas para testes unitários e de integração.

---

<h2 id="started">Configuração e Execução ⚙️</h2>

### Instalação e Execução

1. **Pré-requisitos**:
   - Java 11+
   - Docker
   - PostgreSQL
   - Maven

2. **Clonando o repositório**:
   ```bash
   git clone https://github.com/jpeccia/lariharumi_croche_backend.git
   cd lariharumi_backend
   ```

## 3. Configuração de Variáveis de Ambiente

Este projeto utiliza variáveis de ambiente para gerenciar dados sensíveis, como credenciais de banco de dados, tokens de autenticação e chaves de API. Usar variáveis de ambiente ajuda a manter o código mais seguro e facilita a configuração em diferentes ambientes (desenvolvimento, teste, produção).

### 1. Criar o arquivo `.env`

1. Na raiz do projeto, crie um arquivo chamado `.env`.
2. Adicione as variáveis de ambiente necessárias para o projeto, conforme o exemplo abaixo:

```plaintext
# Configurações de Banco de Dados
DB_USERNAME=seuUsuario
DB_PASSWORD=suaSenha
DB_URL=jdbc:postgresql://localhost:5432/levelinglife

# JWT Token para autenticação
JWT_SECRET=suaChaveSecreta

# Outras configurações importantes
APP_PORT=8080
```

> **Nota:** Este arquivo não deve ser commitado no repositório, pois contém informações sensíveis. Garanta que o `.env` está listado no seu arquivo `.gitignore`.

### 2. Carregar as Variáveis de Ambiente

O Spring Boot carregará automaticamente as variáveis do sistema ou do ambiente. Se você estiver usando o arquivo `.env` com ferramentas como Docker ou outros sistemas de integração, ele será lido automaticamente se configurado no `docker-compose` ou no ambiente do servidor.

### 3. Exemplo de Configuração no `application.properties`

No arquivo `application.properties`, você pode acessar as variáveis usando a sintaxe `${NOME_VARIAVEL}`:

```properties
# Configuração do Banco de Dados
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# Configuração do JWT
jwt.secret=${JWT_SECRET}

# Porta da aplicação
server.port=${APP_PORT}
```

### 4. Usar Variáveis de Ambiente no Docker

Se você estiver usando Docker, defina as variáveis de ambiente no `docker-compose.yml`:

```yaml
services:
  app:
    image: postgres
    environment:
      - DB_USERNAME=${DB_USERNAME}
      - DB_PASSWORD=${DB_PASSWORD}
      - JWT_SECRET=${JWT_SECRET}
      - APP_PORT=${APP_PORT}
```

4. **Rodando a aplicação com Docker**:
   - Execute o comando abaixo para iniciar a aplicação em um container Docker:
     ```bash
     docker-compose up --build
     ```

<h2 id="contribute"></h2>

## Contribuição 🤝

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou pull requests.

---

## Licença 📄

Este projeto está licenciado sob a MIT License.
