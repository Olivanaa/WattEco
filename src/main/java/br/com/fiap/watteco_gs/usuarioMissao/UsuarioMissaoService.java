package br.com.fiap.watteco_gs.usuarioMissao;

import br.com.fiap.watteco_gs.enums.StatusMissaoEnum;
import br.com.fiap.watteco_gs.missao.Missao;
import br.com.fiap.watteco_gs.usuario.Usuario;
import br.com.fiap.watteco_gs.usuario.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UsuarioMissaoService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMissaoRepository usuarioMissaoRepository;

    public UsuarioMissaoService(UsuarioRepository usuarioRepository, UsuarioMissaoRepository usuarioMissaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMissaoRepository = usuarioMissaoRepository;
    }

    public void associarMissaoUsuario(Usuario usuario, Missao missao){
        Optional<UsuarioMissao> usuarioMissaoExistente = usuarioMissaoRepository.findByUsuarioIdAndMissaoId(usuario.getId(), missao.getId());
        if (!usuarioMissaoExistente.isPresent()) {
            UsuarioMissaoId id = new UsuarioMissaoId();
            id.setUsuarioId(usuario.getId());
            id.setMissaoId(missao.getId());

            UsuarioMissao usuarioMissao = new UsuarioMissao();
            usuarioMissao.setId(id);
            usuarioMissao.setUsuario(usuario);
            usuarioMissao.setMissao(missao);
            usuarioMissao.setStatus(StatusMissaoEnum.NAO_COMPLETA);

            usuarioMissaoRepository.save(usuarioMissao);
        }
    }

    public List<UsuarioMissao> listarPorStatus(Long usuarioId, StatusMissaoEnum status){
        return usuarioMissaoRepository.findByUsuarioIdAndStatus(usuarioId, status);
    }


    public void atualizarStatusMissao(Long usuarioId, Long missaoId, StatusMissaoEnum novoStatus) {
        Optional<UsuarioMissao> usuarioMissaoOpt = usuarioMissaoRepository.findByUsuarioIdAndMissaoId(usuarioId, missaoId);
        if (usuarioMissaoOpt.isPresent()) {
            UsuarioMissao usuarioMissao = usuarioMissaoOpt.get();
            usuarioMissao.setStatus(novoStatus);
            usuarioMissaoRepository.save(usuarioMissao);
        } else {
            throw new RuntimeException("Missão não encontrada para o usuário.");
        }
    }

    public void atualizarDataInicio(Long usuarioId, Long missaoId) {
        Optional<UsuarioMissao> usuarioMissaoOpt = usuarioMissaoRepository.findByUsuarioIdAndMissaoId(usuarioId, missaoId);
        if (usuarioMissaoOpt.isPresent()) {
            UsuarioMissao usuarioMissao = usuarioMissaoOpt.get();

            if (usuarioMissao.getStatus() == StatusMissaoEnum.EM_ANDAMENTO && usuarioMissao.getDataInicio() == null) {
                usuarioMissao.setDataInicio(LocalDate.now());
                usuarioMissaoRepository.save(usuarioMissao);
            }
        } else {
            throw new RuntimeException("Missão não encontrada para o usuário.");
        }
    }

    public void atualizarDataConclusao(Long usuarioId, Long missaoId) {
        Optional<UsuarioMissao> usuarioMissaoOpt = usuarioMissaoRepository.findByUsuarioIdAndMissaoId(usuarioId, missaoId);
        if (usuarioMissaoOpt.isPresent()) {
            UsuarioMissao usuarioMissao = usuarioMissaoOpt.get();

            if (usuarioMissao.getStatus() == StatusMissaoEnum.COMPLETA && usuarioMissao.getDataConclusao() == null) {
                usuarioMissao.setDataConclusao(LocalDate.now());
                usuarioMissaoRepository.save(usuarioMissao);
            }
        } else {
            throw new RuntimeException("Missão não encontrada para o usuário.");
        }
    }

    public void atualizarPontosAcumulados(Long missaoId, Long usuarioId) {
        Optional<UsuarioMissao> usuarioMissaoOpt = usuarioMissaoRepository.findByUsuarioIdAndMissaoId(usuarioId, missaoId);

        if (usuarioMissaoOpt.isPresent()) {
            UsuarioMissao usuarioMissao = usuarioMissaoOpt.get();
            Usuario usuario = usuarioMissao.getUsuario();

            if (usuario != null) {
                int pontosMissao = usuarioMissao.getMissao().getPontos();
                log.info("Pontos atuais: {}", usuario.getPontosAcumulados());
                log.info("Pontos da missão: {}", pontosMissao);

                usuario.setPontosAcumulados(usuario.getPontosAcumulados() + pontosMissao);
                usuarioRepository.save(usuario);

                log.info("Pontos atualizados: {}", usuario.getPontosAcumulados());
            } else {
                throw new RuntimeException("Usuário não encontrado.");
            }
        } else {
            throw new RuntimeException("Missão não encontrada para o usuário.");
        }
    }
}
