CREATE TABLE watteco_usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR2(200) NOT NULL,
    email VARCHAR2(255) NOT NULL,
    senha VARCHAR2(255) NOT NULL,
    telefone VARCHAR2(11) NOT NULL,
    dtaNascimento DATE NOT NULL,
    dtaCadastro DATE DEFAULT SYSDATE NOT NULL,
    pontosAcumulados NUMBER DEFAULT 0,
    avatar VARCHAR2(255),
    CONSTRAINT pk_wattUsuario PRIMARY KEY (id)
);