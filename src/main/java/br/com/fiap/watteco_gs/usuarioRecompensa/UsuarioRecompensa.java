package br.com.fiap.watteco_gs.usuarioRecompensa;

import br.com.fiap.watteco_gs.enums.StatusRecompensaEnum;
import br.com.fiap.watteco_gs.recompensa.Recompensa;
import br.com.fiap.watteco_gs.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "usuario_recompensa")
public class UsuarioRecompensa {

    @EmbeddedId
    private UsuarioRecompensaId id;

    @ManyToOne
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @MapsId("recompensaId")
    @JoinColumn(name= "recompensa_id")
    private Recompensa recompensa;

    @Enumerated(EnumType.STRING)
    private StatusRecompensaEnum status;

    private LocalDate dataResgate;
}
