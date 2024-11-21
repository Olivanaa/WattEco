package br.com.fiap.watteco_gs.usuarioRecompensa;

import br.com.fiap.watteco_gs.enums.StatusRecompensaEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRecompensaRepository extends JpaRepository<UsuarioRecompensa, UsuarioRecompensaId> {
    List<UsuarioRecompensa> findByUsuarioIdAndStatus(Long usuarioId, StatusRecompensaEnum status);

    Optional<UsuarioRecompensa> findByUsuarioIdAndRecompensaId(Long usuarioId, Long recompensaId);

}
