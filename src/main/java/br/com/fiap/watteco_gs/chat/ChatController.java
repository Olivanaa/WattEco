package br.com.fiap.watteco_gs.chat;

import br.com.fiap.watteco_gs.usuario.Usuario;
import br.com.fiap.watteco_gs.usuario.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final MessageProducer producer;
    private final UsuarioService usuarioService;
    private final ChatService chatService;

    public ChatController(MessageProducer producer, UsuarioService usuarioService, ChatService chatService) {
        this.producer = producer;
        this.usuarioService = usuarioService;
        this.chatService = chatService;
    }

    @GetMapping
    public String showChatPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(username);
        model.addAttribute("messages", new ArrayList<String>());
        return "chat";
    }

    @PostMapping
    public String sendMessage(@RequestParam String userMessage, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(username);
        producer.sendMessage(userMessage);

        List<String> messages = (List<String>) model.getAttribute("messages");
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add("Você: " + userMessage);

        String aiResponse = chatService.sendToAi(userMessage);
        messages.add("IA: " + aiResponse);

        model.addAttribute("messages", messages);
        return "chat";
    }

}
