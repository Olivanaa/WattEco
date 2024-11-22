package br.com.fiap.watteco_gs.usuarioRecompensa;

import br.com.fiap.watteco_gs.enums.StatusRecompensaEnum;
import br.com.fiap.watteco_gs.recompensa.Recompensa;
import br.com.fiap.watteco_gs.usuario.Usuario;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UsuarioRecompensaService {

    private final UsuarioRecompensaRepository usuarioRecompensaRepository;

    public UsuarioRecompensaService(UsuarioRecompensaRepository usuarioRecompensaRepository) {
        this.usuarioRecompensaRepository = usuarioRecompensaRepository;
    }

    public void associarRecompensaUsuario(Usuario usuario, Recompensa recompensa) {
        Optional<UsuarioRecompensa> usuarioRecompensaExistente = usuarioRecompensaRepository.findByUsuarioIdAndRecompensaId(usuario.getId(), recompensa.getId());
        if (!usuarioRecompensaExistente.isPresent()) {
            UsuarioRecompensaId id = new UsuarioRecompensaId();
            id.setUsuarioId(usuario.getId());
            id.setRecompensaId(recompensa.getId());

            UsuarioRecompensa usuarioRecompensa = new UsuarioRecompensa();
            usuarioRecompensa.setId(id);
            usuarioRecompensa.setUsuario(usuario);
            usuarioRecompensa.setRecompensa(recompensa);
            usuarioRecompensa.setStatus(StatusRecompensaEnum.NAO_RESGATADA);
            usuarioRecompensaRepository.save(usuarioRecompensa);
        }
    }

    public List<UsuarioRecompensa> listarPorStatus(Long usuarioId, StatusRecompensaEnum status) {
        return usuarioRecompensaRepository.findByUsuarioIdAndStatus(usuarioId, status);
    }

    @Transactional
    public void atualizarStatusRecompensa(Long usuarioId, Long recompensaId, StatusRecompensaEnum status) {
        Optional<UsuarioRecompensa> usuarioRecompensaOpt = usuarioRecompensaRepository.findByUsuarioIdAndRecompensaId(usuarioId, recompensaId);
        if (usuarioRecompensaOpt.isPresent()) {
            UsuarioRecompensa usuarioRecompensa = usuarioRecompensaOpt.get();
            Usuario usuario = usuarioRecompensa.getUsuario();

            if(usuario != null){
                int pontosRecompensa = usuarioRecompensa.getRecompensa().getPontosNecessarios();
                int pontosUsuario = usuarioRecompensa.getUsuario().getPontosAcumulados();
                if (usuarioRecompensa.getStatus().equals(StatusRecompensaEnum.NAO_RESGATADA)) {
                    if (pontosUsuario >= pontosRecompensa) {
                        usuarioRecompensa.setStatus(StatusRecompensaEnum.RESGATADA);
                        usuarioRecompensaRepository.save(usuarioRecompensa);
                    } else {
                        throw new RuntimeException("Pontos insuficientes para resgatar esta recompensa.");
                    }
                }
            }
        }
    }
}
