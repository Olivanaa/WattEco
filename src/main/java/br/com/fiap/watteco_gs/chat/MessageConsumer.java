package br.com.fiap.watteco_gs.chat;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    private final ChatService chatService;

    @Autowired
    public MessageConsumer(ChatService chatService) {
        this.chatService = chatService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("Mensagem recebida: " + message);
        String response = chatService.sendToAi(message);
        System.out.println("Resposta gerada pela IA: " + response);
    }
}
