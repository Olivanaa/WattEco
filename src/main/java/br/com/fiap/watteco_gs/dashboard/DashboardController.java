package br.com.fiap.watteco_gs.dashboard;

import br.com.fiap.watteco_gs.enums.StatusMissaoEnum;
import br.com.fiap.watteco_gs.usuario.Usuario;
import br.com.fiap.watteco_gs.usuario.UsuarioService;
import br.com.fiap.watteco_gs.usuarioMissao.UsuarioMissao;
import br.com.fiap.watteco_gs.usuarioMissao.UsuarioMissaoService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final UsuarioMissaoService usuarioMissaoService;
    private final UsuarioService usuarioService;

    public DashboardController(UsuarioMissaoService usuarioMissaoService, UsuarioService usuarioService) {
        this.usuarioMissaoService = usuarioMissaoService;
        this.usuarioService = usuarioService;
    }
    @GetMapping
    public String mostrarMissoesNaoCompletadas(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Usuario usuario = usuarioService.buscarPorEmail(username);

        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            List<UsuarioMissao> missoes = usuarioMissaoService.listarPorStatus(usuario.getId(), StatusMissaoEnum.NAO_COMPLETA);
            model.addAttribute("missoes", missoes);
            model.addAttribute("avatar", usuario.getAvatar());
        }

        return "dashboard";
    }
}
