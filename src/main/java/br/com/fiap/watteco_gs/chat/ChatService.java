package br.com.fiap.watteco_gs.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        Você é um especialista em economia de energia elétrica doméstica.
                        Responda de maneira cordial.
                        Responda apenas perguntas relacionadas com consumo de energia.
                        Se não souber a resposta, diga que não sabe.
                        """)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory())
                )
                .build();
    }

    public String sendToAi(String message) {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .content();
    }
}
