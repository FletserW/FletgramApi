-- Tabela para armazenar informações do usuário
CREATE TABLE users (
    id SERIAL PRIMARY KEY,               -- ID único do usuário
    full_name VARCHAR(255) NOT NULL,      -- Nome completo
    username VARCHAR(50) UNIQUE NOT NULL, -- Nome de usuário único
    email VARCHAR(255) UNIQUE,            -- E-mail (opcional)
    phone VARCHAR(15) UNIQUE,             -- Telefone (opcional)
    password_hash VARCHAR(255) NOT NULL,  -- Senha (armazenada de forma segura, como hash)
    email_verified BOOLEAN DEFAULT FALSE, -- Se o e-mail foi verificado (pode ser útil)
    phone_verified BOOLEAN DEFAULT FALSE,-- Se o telefone foi verificado
    profile_picture VARCHAR(255),         -- URL da foto de perfil (opcional)
    bio TEXT,                             -- Biografia (opcional)
    date_of_birth DATE,                   -- Data de nascimento (opcional)
    gender VARCHAR(10),                   -- Gênero (opcional)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Data de criação da conta
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Data de atualização
);

-- Tabela para registrar a verificação de e-mail (para implementação de e-mail de verificação)
CREATE TABLE email_verification (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id), -- Relaciona com a tabela de usuários
    token VARCHAR(255) NOT NULL, -- Token de verificação único
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Data de criação do token
    expires_at TIMESTAMP, -- Data de expiração do token
    verified BOOLEAN DEFAULT FALSE -- Se o e-mail foi verificado
);

-- Tabela para registrar a verificação de telefone (para implementação de SMS de verificação)
CREATE TABLE phone_verification (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id), -- Relaciona com a tabela de usuários
    token VARCHAR(255) NOT NULL, -- Token de verificação único para telefone
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Data de criação do token
    expires_at TIMESTAMP, -- Data de expiração do token
    verified BOOLEAN DEFAULT FALSE -- Se o telefone foi verificado
);

-- Tabela para gerenciar posts (simplificada)
CREATE TABLE posts (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id), -- Relaciona com a tabela de usuários
    content TEXT,                             -- Conteúdo do post (pode ser texto ou URL de imagem/vídeo)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Data de criação do post
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Data de atualização do post
);

-- Tabela para gerenciar seguidores (seguindo e sendo seguido)
CREATE TABLE followers (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id),  -- Quem está seguindo
    follower_id INT NOT NULL REFERENCES users(id), -- Quem está sendo seguido
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Data de seguimento
);

-- Tabela para gerenciar curtidas (posts que o usuário curtiu)
CREATE TABLE likes (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id), -- Quem deu a curtida
    post_id INT NOT NULL REFERENCES posts(id), -- Post que foi curtido
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Data da curtida
);

-- Tabela para armazenar imagens de posts
CREATE TABLE post_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT(20) UNSIGNED NOT NULL,  -- Ajuste para usar `BIGINT(20) UNSIGNED` aqui
    image_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

