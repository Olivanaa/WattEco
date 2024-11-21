package br.com.fiap.watteco_gs.usuarioMissao;

import br.com.fiap.watteco_gs.enums.StatusMissaoEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioMissaoRepository extends JpaRepository<UsuarioMissao, UsuarioMissaoId> {
    List<UsuarioMissao> findByUsuarioIdAndStatus(Long usuarioId, StatusMissaoEnum status);

    Optional<UsuarioMissao> findByUsuarioIdAndMissaoId(Long usuarioId, Long missaoId);
}
