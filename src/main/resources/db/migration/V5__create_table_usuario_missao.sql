CREATE TABLE usuario_missao (
    usuario_id NUMBER,
    missao_id NUMBER,
    status VARCHAR2(50) NOT NULL,
    data_inicio DATE,
    data_conclusao DATE,
    PRIMARY KEY (usuario_id, missao_id),
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES watt_usuario(id),
    CONSTRAINT fk_missao FOREIGN KEY (missao_id) REFERENCES watt_missao(id)
);