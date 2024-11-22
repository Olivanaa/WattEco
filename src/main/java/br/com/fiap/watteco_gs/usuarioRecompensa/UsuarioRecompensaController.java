package br.com.fiap.watteco_gs.usuarioRecompensa;

import br.com.fiap.watteco_gs.enums.StatusRecompensaEnum;
import br.com.fiap.watteco_gs.usuario.Usuario;
import br.com.fiap.watteco_gs.usuario.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/recompensas")
@Slf4j
public class UsuarioRecompensaController {

    private final UsuarioService usuarioService;
    private final UsuarioRecompensaService usuarioRecompensaService;

    public UsuarioRecompensaController(UsuarioService usuarioService, UsuarioRecompensaService usuarioRecompensaService) {
        this.usuarioService = usuarioService;
        this.usuarioRecompensaService = usuarioRecompensaService;
    }

    @GetMapping
    public String listarRecompensas(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(username);
        if(usuario != null) {;
            model.addAttribute("usuario", usuario);
            List<UsuarioRecompensa> recompensasResgatadas = usuarioRecompensaService.listarPorStatus(usuario.getId(), StatusRecompensaEnum.RESGATADA);
            List<UsuarioRecompensa> recompensasNaoResgatadas = usuarioRecompensaService.listarPorStatus(usuario.getId(), StatusRecompensaEnum.NAO_RESGATADA);
            model.addAttribute("recompensasResgatadas", recompensasResgatadas);
            model.addAttribute("recompensasNaoResgatadas", recompensasNaoResgatadas);
            model.addAttribute("pontos", usuario.getPontosAcumulados());
            model.addAttribute("avatar", usuario.getAvatar());
        }
        return "recompensas";
    }

    @PostMapping("/atualizar/resgatada")
    public String alterarStatusRecompensa(@RequestParam("recompensaId") String recompensaId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(username);
        Long recompensaIdLong = Long.parseLong(recompensaId);

        try{
            usuarioRecompensaService.atualizarStatusRecompensa(usuario.getId(), recompensaIdLong, StatusRecompensaEnum.RESGATADA);
            log.info("ID da recompensa enviado no formulário: {}", recompensaId);
            log.info("Status da recompensa atualizado com sucesso para o usuário {}", usuario.getEmail());

        } catch (RuntimeException e) {
            log.error("Erro ao atualizar o status: {}", e.getMessage(), e);
            if ("Pontos insuficientes para resgatar esta recompensa.".equals(e.getMessage())) {
                model.addAttribute("error", e.getMessage());
                return "recompensas";
            }
        }
        return "redirect:/recompensas";
    }
}
