package br.com.fiap.watteco_gs.usuarioMissao;

import br.com.fiap.watteco_gs.enums.StatusMissaoEnum;
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
@RequestMapping("/missoes")
@Slf4j
public class UsuarioMissaoController {

    private final UsuarioMissaoService usuarioMissaoService;
    private final UsuarioService usuarioService;

    public UsuarioMissaoController(UsuarioMissaoService usuarioMissaoService, UsuarioService usuarioService) {
        this.usuarioMissaoService = usuarioMissaoService;
        this.usuarioService = usuarioService;
    }


    @GetMapping
    public String listarMissoes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(username);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            List<UsuarioMissao> missoesEmAndamento = usuarioMissaoService.listarPorStatus(usuario.getId(), StatusMissaoEnum.EM_ANDAMENTO);
            List<UsuarioMissao> missoesCompletas = usuarioMissaoService.listarPorStatus(usuario.getId(), StatusMissaoEnum.COMPLETA);
            model.addAttribute("missoesEmAndamento", missoesEmAndamento);
            model.addAttribute("missoesCompletas", missoesCompletas);
            model.addAttribute("avatar", usuario.getAvatar());
        }
        return "missoes";
    }

    @PostMapping("/atualizar/emandamento")
    public String alterarStatusEmAndamento(@RequestParam("missaoId") String missaoId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(username);
        Long missaoIdLong = Long.parseLong(missaoId);
        if (usuario == null) {
            model.addAttribute("error", "Usuário não encontrado");
            return "erro";
        }
        try {
            usuarioMissaoService.atualizarStatusMissao(usuario.getId(), missaoIdLong, StatusMissaoEnum.EM_ANDAMENTO);
            usuarioMissaoService.atualizarDataInicio(usuario.getId(), missaoIdLong);
        } catch (RuntimeException e) {
            log.error("Erro ao atualizar o status da missão: {}", e.getMessage(), e);
        }

        return "redirect:/missoes";
    }

    @PostMapping("/atualizar/concluida")
    public String alterarStatusConcluida(@RequestParam("missaoId") String missaoId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(username);
        Long missaoIdLong = Long.parseLong(missaoId);
        if (usuario == null) {
            model.addAttribute("error", "Usuário não encontrado");
            return "erro";
        }
        try {
            usuarioMissaoService.atualizarStatusMissao(usuario.getId(), missaoIdLong, StatusMissaoEnum.COMPLETA);
            usuarioMissaoService.atualizarDataConclusao(usuario.getId(), missaoIdLong);
            usuarioMissaoService.atualizarPontosAcumulados(missaoIdLong, usuario.getId());
        } catch (RuntimeException e) {
            log.error("Erro ao atualizar o status da missão: {}", e.getMessage(), e);
        }

        return "redirect:/missoes";
    }
}
