package br.com.fiap.watteco_gs.chat;

import br.com.fiap.watteco_gs.usuario.Usuario;
import br.com.fiap.watteco_gs.usuario.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final MessageProducer producer;
    private final UsuarioService usuarioService;
    private final ChatService chatService;
    private final MessageConsumer consumer;

    public ChatController(MessageProducer producer, UsuarioService usuarioService, ChatService chatService, MessageConsumer consumer) {
        this.producer = producer;
        this.usuarioService = usuarioService;
        this.chatService = chatService;
        this.consumer = consumer;
    }

    @GetMapping
    public String showChatPage(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(username);

        List<String> userMessages = (List<String>) session.getAttribute("userMessages");
        List<String> aiMessages = (List<String>) session.getAttribute("aiMessages");

        model.addAttribute("usuario", usuario.getAvatar());
        model.addAttribute("userMessages", userMessages != null ? userMessages : new ArrayList<>());
        model.addAttribute("aiMessages", aiMessages != null ? aiMessages : new ArrayList<>());

        return "chat";
    }

    @PostMapping
    public String sendMessage(@RequestParam String userMessage, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(username);
        producer.sendMessage(userMessage);

        List<String> userMessages = new ArrayList<>(consumer.getMessages());
        userMessages.add("Você: " + userMessage);

        String aiResponse = chatService.sendToAi(userMessage);
        List<String> aiMessages = new ArrayList<>();
        aiMessages.add("IA: " + aiResponse);

        System.out.println("Mensagens do usuário: " + userMessages);
        System.out.println("Mensagens da IA: " + aiMessages);

        session.setAttribute("userMessages", userMessages);
        session.setAttribute("aiMessages", aiMessages);
        session.setAttribute("usuario", usuario.getAvatar());

        return "redirect:/chat";
    }

}