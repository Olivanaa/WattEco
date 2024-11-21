CREATE TABLE IF NOT EXISTS watteco_recompensa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR2(255) NOT NULL,
    pontosNecessarios NUMBER(10) NOT NULL
);