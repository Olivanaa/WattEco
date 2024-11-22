CREATE TABLE usuario_recompensa (
    usuario_id NUMBER,
    recompensa_id NUMBER,
    status VARCHAR2(50) NOT NULL,
    data_resgate DATE,
    PRIMARY KEY (usuario_id, recompensa_id),
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES watt_usuario(id),
    CONSTRAINT fk_recompensa FOREIGN KEY (recompensa_id) REFERENCES watt_recompensa(id)
);