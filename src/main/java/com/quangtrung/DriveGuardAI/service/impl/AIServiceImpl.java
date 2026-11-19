package com.quangtrung.DriveGuardAI.service.impl;

import com.quangtrung.DriveGuardAI.service.AIService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIServiceImpl implements AIService {
    private final ChatClient chatClient;

    public AIServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    @Override
    public String askAi(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
