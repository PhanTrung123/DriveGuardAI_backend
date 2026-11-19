package com.quangtrung.DriveGuardAI.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Bean
    public ChatClient chatClient(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("Bạn là trợ lý ảo của hệ thống DriveGuardAI, trả lời ngắn gọn, chính xác bằng tiếng Việt.")
                .build();
    }
}
