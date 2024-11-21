package br.com.fiap.watteco_gs.usuarioMissao;

import br.com.fiap.watteco_gs.enums.StatusMissaoEnum;
import br.com.fiap.watteco_gs.missao.Missao;
import br.com.fiap.watteco_gs.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "usuario_missao")
public class UsuarioMissao {

    @EmbeddedId
    private UsuarioMissaoId id;

    @ManyToOne
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @MapsId("missaoId")
    @JoinColumn(name = "missao_id")
    private Missao missao;

    @Enumerated(EnumType.STRING)
    private StatusMissaoEnum status;

    private LocalDate dataInicio;
    private LocalDate dataConclusao;
}
