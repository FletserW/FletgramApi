# Fletgram - Backend

## 📌 Sobre o Projeto
O **Fletgram** é um projeto de rede social inspirado no Instagram, desenvolvido com fins educativos. O backend é construído usando **Spring Boot** e **MySQL**, oferecendo suporte a funcionalidades como posts, curtidas, comentários, mensagens diretas, stories e reels.

⚠️ **Este projeto é apenas para aprendizado e não deve ser usado comercialmente.**

## 🛠️ Tecnologias Utilizadas
- **Java** com **Spring Boot**
- **MySQL** para o banco de dados
- **JPA/Hibernate** para ORM
- **Spring Security** para autenticação
- **JWT (JSON Web Token)** para segurança
- **Maven** para gerenciamento de dependências
- **LocalTunnel** para expor a API publicamente durante o desenvolvimento

## 📂 Estrutura do Projeto
```
backend/
│── midia/ # Armazena midia de conversas
│── src/
│   ├── main/java/com/Fletsertech/Fletgram/
│   │   ├── Configurações/ 
│   │   ├── controller/   # Controladores REST
│   │   ├── dto/          # Data Transfer Objects
│   │   ├── entity/       # Entidades do banco de dados
│   │   ├── repository/   # Repositórios (JPA)
│   │   ├── service/      # Regras de negócio
│   │   ├── security/     # Configuração de segurança (JWT)
│   ├── resources/
│   │   ├── application.properties  # Configuração do banco de dados
│   │   ├── static/apk/ # Armazenar apk
│── pom.xml  # Dependências do Maven
│── Uploads/  # Armazena postagens e foto de perfil
│── Bd.sql  # Estrutura do banco de dados
```

## 🗄️ Configuração do Banco de Dados
Aqui está a estrutura do banco de dados utilizada no backend:
```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(15) UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email_verified BOOLEAN DEFAULT FALSE,
    phone_verified BOOLEAN DEFAULT FALSE,
    profile_picture VARCHAR(255),
    bio TEXT,
    date_of_birth DATE,
    gender VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🔧 Configuração do Ambiente
### 1️⃣ Clone o Repositório
```sh
git clone https://github.com/FletserW/FletgramApi.git

```
### 2️⃣ Configure o Banco de Dados
No arquivo `src/main/resources/application.properties`, configure os detalhes do MySQL:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fletgrambd
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3️⃣ Execute o Backend
```sh
mvn spring-boot:run
```
A API estará disponível em `https://fletgram.loca.lt`.

## 🔑 Autenticação com JWT
O sistema utiliza **JSON Web Token (JWT)** para autenticação. Para acessar endpoints protegidos:
1. Faça login (`/users/login`) e receba um token JWT.

## 🌐 Expondo a API com LocalTunnel
Caso queira testar o backend remotamente, utilize o **LocalTunnel**:
```sh
lt --port 8080 --subdomain fletgram-api
```
Agora sua API estará acessível em `https://fletgram-api.loca.lt`.

## 🚀 Funcionalidades Implementadas
- ✅ Autenticação JWT
- ✅ Cadastro e login de usuários
- ✅ Upload de imagens de perfil
- ✅ Seguir/Deixar de seguir usuários
- ✅ Criar, editar e excluir posts
- ✅ Curtidas e comentários
- ✅ Sistema de mensagens diretas
- ✅ Stories e Reels
- ✅ Atualização automática do APK

## 📖 API Endpoints Principais
| Método | Rota                           | Descrição                         |
|--------|--------------------------------|----------------------------------|
| POST   | `/auth/register`               | Cadastro de usuário             |
| POST   | `/auth/login`                  | Login e geração de JWT          |
| GET    | `/users/{id}`                  | Obtém informações do usuário    |
| POST   | `/posts`                        | Criar um novo post              |
| GET    | `/posts/{id}`                   | Obter um post específico        |
| POST   | `/likes/{postId}`               | Curtir um post                  |
| POST   | `/comments/{postId}`            | Comentar em um post             |
| GET    | `/messages/conversation/{id}`   | Buscar mensagens de uma conversa |

O restante dos endpoints podem ser encontrados em [swagger-ui](http://localhost:8082/swagger-ui/index.html)
## 📜 Licença
Este projeto é open-source e está sob a licença MIT.

---
🔥 Desenvolvido para fins educacionais! Caso tenha sugestões, contribuições ou queira aprender mais, sinta-se à vontade para abrir uma **issue** ou um **pull request**. 🚀


