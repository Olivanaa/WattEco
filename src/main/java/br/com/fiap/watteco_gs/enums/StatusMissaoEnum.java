package br.com.fiap.watteco_gs.enums;

public enum StatusMissaoEnum {
    NAO_COMPLETA("não completa"),
    EM_ANDAMENTO("em andamento"),
    COMPLETA("completa");

    private String descricao;

    StatusMissaoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
