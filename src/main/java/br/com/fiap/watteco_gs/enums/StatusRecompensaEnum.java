package br.com.fiap.watteco_gs.enums;

public enum StatusRecompensaEnum {
    NAO_RESGATADA("não resgatada"),
    RESGATADA("resgatada");

    private String descricao;

    StatusRecompensaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
