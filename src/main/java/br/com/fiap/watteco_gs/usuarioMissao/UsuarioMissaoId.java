package br.com.fiap.watteco_gs.usuarioMissao;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class UsuarioMissaoId implements Serializable {

    private Long usuarioId;
    private Long missaoId;
}
