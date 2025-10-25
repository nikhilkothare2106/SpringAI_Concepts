package com.ai.service;

import com.ai.advisor.TokenAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {

    @Value("classpath:/prompts/user-message.st")
    private Resource userMessage;

    @Value("classpath:/prompts/system-message.st")
    private Resource systemMessage;


    private ChatClient chatClient;

    public ChatService(ChatClient chatClient){
        this.chatClient = chatClient;
    }
    public String chat(String q) {
        return chatClient
                .prompt(q)
                .advisors(new SimpleLoggerAdvisor(),new TokenAdvisor())
                .system(system->system.text(systemMessage))
                .user(user->user.text(userMessage))
                .call()
                .content();

    }

    public Flux<String> streamChat(String q) {
        return chatClient
                .prompt(q)
                .advisors(new SimpleLoggerAdvisor(),new TokenAdvisor())
                .system(system->system.text(systemMessage))
                .user(user->user.text(userMessage))
                .stream()
                .content();
    }
}
