CREATE TABLE watteco_missao (
    id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    titulo VARCHAR2(255) NOT NULL,
    descricao VARCHAR2(255) NOT NULL,
    pontos NUMBER NOT NULL,
    meta VARCHAR2(255) NOT NULL
);