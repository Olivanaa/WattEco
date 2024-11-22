package br.com.fiap.watteco_gs.consumo;

import br.com.fiap.watteco_gs.usuario.Usuario;
import br.com.fiap.watteco_gs.usuario.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/consumo")
@Slf4j
public class ConsumoController {

    private final ConsumoService consumoService;
    private final UsuarioService usuarioService;

    public ConsumoController(ConsumoService consumoService, UsuarioService usuarioService) {
        this.consumoService = consumoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/todos")
    public String mostrarConsumo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(username);
        List<Consumo> registros = consumoService.buscarTodos();
        model.addAttribute("usuario", usuario);
        model.addAttribute("consumos", registros);
        return "consumo/index";
    }

    @GetMapping("/adicionar")
    public String mostrarFormulario(Model model, Consumo consumo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(username);
        model.addAttribute("usuario", usuario);
        model.addAttribute("consumos", consumo);
        return "consumo/create";
    }

    @PostMapping("/adicionar")
    public String adicionarConsumo(Model model, Consumo consumo, BindingResult result) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Usuario usuario = usuarioService.buscarPorEmail(username);

        if (result.hasErrors()) {
            model.addAttribute("consumo", consumo);
            model.addAttribute("usuario", usuario);
            log.error(result.getAllErrors().toString());
            return "consumo/create";
        }

        consumoService.criar(consumo);
        return "redirect:/consumo/todos";
    }
}
