package br.com.fiap.watteco_gs.usuarioRecompensa;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class UsuarioRecompensaId implements Serializable {

    private Long usuarioId;
    private Long recompensaId;

}
