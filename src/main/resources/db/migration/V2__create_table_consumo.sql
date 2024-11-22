CREATE TABLE watteco_registro_consumo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dataRegistro DATE NOT NULL,
    consumo NUMBER NOT NULL,
    usuario_id NUMBER,
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES watt_usuario(id)
);