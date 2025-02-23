-- Tabela para armazenar informações do usuário
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,               -- ID único do usuário
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
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id), -- Relaciona com a tabela de usuários
    token VARCHAR(255) NOT NULL, -- Token de verificação único
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Data de criação do token
    expires_at TIMESTAMP, -- Data de expiração do token
    verified BOOLEAN DEFAULT FALSE -- Se o e-mail foi verificado
);

-- Tabela para registrar a verificação de telefone (para implementação de SMS de verificação)
CREATE TABLE phone_verification (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id), -- Relaciona com a tabela de usuários
    token VARCHAR(255) NOT NULL, -- Token de verificação único para telefone
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Data de criação do token
    expires_at TIMESTAMP, -- Data de expiração do token
    verified BOOLEAN DEFAULT FALSE -- Se o telefone foi verificado
);

-- Tabela para gerenciar posts (simplificada)
CREATE TABLE posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id), -- Relaciona com a tabela de usuários
    content TEXT,                             -- Conteúdo do post (pode ser texto ou URL de imagem/vídeo)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Data de criação do post
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Data de atualização do post
);

-- Tabela para gerenciar seguidores (seguindo e sendo seguido)
CREATE TABLE followers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id),  -- Quem está seguindo
    follower_id INT NOT NULL REFERENCES users(id), -- Quem está sendo seguido
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Data de seguimento
);

-- Tabela para gerenciar curtidas (posts que o usuário curtiu)
CREATE TABLE likes (
    id INT AUTO_INCREMENT PRIMARY KEY,
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

-- Tabela para conversas (diretas ou em grupo)
CREATE TABLE conversations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    is_group BOOLEAN DEFAULT FALSE,         -- Define se é um grupo ou uma conversa privada
    name VARCHAR(255),                      -- Nome do grupo (NULL para conversas privadas)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

    -- Tabela de participantes das conversas
    CREATE TABLE conversation_participants (
        id INT AUTO_INCREMENT PRIMARY KEY,
        conversation_id INT NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
        user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
        joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        UNIQUE (conversation_id, user_id)  -- Impede duplicação de participantes
    );

    -- Tabela de mensagens dentro das conversas
    CREATE TABLE messages (
        id INT AUTO_INCREMENT PRIMARY KEY,  -- Usando AUTO_INCREMENT em vez de SERIAL
        conversation_id INT NOT NULL,
        sender_id INT NOT NULL,
        message_text TEXT,                  -- Texto da mensagem (NULL se for apenas mídia)
        media_url VARCHAR(255),             -- URL de imagem ou vídeo (NULL se for apenas texto)
        media_type VARCHAR(50) DEFAULT 'none', -- Definindo o valor padrão diretamente sem o CHECK
        edited BOOLEAN DEFAULT FALSE,       -- Indica se a mensagem foi editada
        deleted BOOLEAN DEFAULT FALSE,      -- Indica se a mensagem foi apagada
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        );

-- Tabela para reações às mensagens
CREATE TABLE message_reactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    message_id INT NOT NULL REFERENCES messages(id) ON DELETE CASCADE,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    reaction VARCHAR(50) CHECK (reaction IN ('❤️', '👍', '😂', '😮', '😢', '😡')), -- Reações padrão estilo Instagram
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (message_id, user_id) -- Um usuário pode reagir apenas uma vez por mensagem
);

-- Índices para melhorar a performance
CREATE INDEX idx_messages_conversation ON messages(conversation_id);
CREATE INDEX idx_messages_sender ON messages(sender_id);
CREATE INDEX idx_conversation_participants ON conversation_participants(conversation_id);
CREATE INDEX idx_message_reactions ON message_reactions(message_id);
