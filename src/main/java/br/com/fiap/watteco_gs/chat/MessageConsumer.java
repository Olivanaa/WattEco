package br.com.fiap.watteco_gs.chat;

import br.com.fiap.watteco_gs.config.RabbitMQConfig;
import lombok.Getter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
public class MessageConsumer {

    private final ChatService chatService;
    private final List<String> messages = new ArrayList<>();

    @Autowired
    public MessageConsumer(ChatService chatService) {
        this.chatService = chatService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("Mensagem recebida: " + message);
        String response = chatService.sendToAi(message);
        messages.add("IA: " + response);
        System.out.println("Resposta gerada pela IA: " + response);
    }
}
