package br.com.fiap.watteco_gs.usuario;

import br.com.fiap.watteco_gs.usuario.dto.UsuarioFormRequest;
import br.com.fiap.watteco_gs.usuario.dto.UsuarioUpdateFormRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/user")
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/create")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuarioForm",
                new UsuarioFormRequest(null, null, null, null, null));
        return "user/create";
    }

    @PostMapping("/create")
    public String adicionarUsuario(@Valid @ModelAttribute("usuarioForm") UsuarioFormRequest usuarioForm,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuarioForm", usuarioForm);
            log.error("Erros de validação: {}", bindingResult.getAllErrors());
            return "user/create";
        }

        usuarioService.criar(usuarioForm);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String visualizarPerfil(Model model) {
        Usuario usuario = buscarUsuarioAutenticado();
        model.addAttribute("usuario", usuario);
        return "user/profile";
    }

    @PostMapping("/update-avatar")
    public String atualizarAvatar(@RequestParam("avatar") MultipartFile file, Model model) {
        Usuario usuario = buscarUsuarioAutenticado();
        try {
            usuarioService.atualizarAvatar(usuario.getId(), file);
            return "redirect:/user/profile";
        } catch (MaxUploadSizeExceededException e) {
            log.error("Erro: Arquivo muito grande.", e);
            model.addAttribute("error", "O arquivo enviado é muito grande. Por favor, envie um arquivo menor.");
            return "user/profile";
        } catch (RuntimeException e) {
            log.error("Erro ao atualizar o avatar: {}", e.getMessage());
            model.addAttribute("error", "Erro ao atualizar o avatar. Tente novamente.");
            return "user/profile";
        }
    }

    @GetMapping("/update")
    public String mostrarFormularioAtualizacao(Model model) {
        Usuario usuario = buscarUsuarioAutenticado();
        try {
            UsuarioUpdateFormRequest usuarioFormRequest = new UsuarioUpdateFormRequest(usuario.getNome(), usuario.getTelefone());
            model.addAttribute("usuarioForm", usuarioFormRequest);
            return "user/update";
        } catch (ResponseStatusException e) {
            log.error("Erro: {}", e.getMessage());
            return "redirect:/user/profile";
        }
    }

    @PostMapping("/update")
    public String atualizarPerfil(@Valid @ModelAttribute("usuarioForm") UsuarioUpdateFormRequest usuarioForm,
                                  BindingResult bindingResult, Model model) {

        Usuario usuario = buscarUsuarioAutenticado();
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuarioForm", usuarioForm);
            log.error("Erro na validação dos dados: {}", bindingResult.getAllErrors());
            return "user/update";
        }

        try {
            usuarioService.atualizar(usuario.getId(), usuarioForm);
            return "redirect:/user/profile";
        } catch (ResponseStatusException e) {
            log.error("Erro ao atualizar o perfil: {}", e.getMessage());
            model.addAttribute("error", "Erro ao atualizar o perfil. Tente novamente.");
            return "user/update";
        }
    }

    private Usuario buscarUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(username);

        if (usuario == null) {
            log.error("Usuário autenticado não encontrado.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        return usuario;
    }
}
