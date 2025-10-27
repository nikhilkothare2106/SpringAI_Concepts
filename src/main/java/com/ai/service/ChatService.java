package com.ai.service;

import com.ai.advisor.TokenAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@Service
public class ChatService {

    @Value("classpath:/prompts/user-message.st")
    private Resource userMessage;

    @Value("classpath:/prompts/system-message.st")
    private Resource systemMessage;


    private ChatClient chatClient;
    private VectorStore vectorStore;

    public ChatService(ChatClient chatClient, VectorStore vectorStore){
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }
    public String chat(String q,String userId) {

        SearchRequest searchRequest = SearchRequest
                .builder()
                .topK(5)
                .similarityThreshold(0.5)
                .query(q)
                .build();

        List<Document> documents = vectorStore.similaritySearch(searchRequest);
        List<String> list = documents.stream().map(Document::getText).toList();
        String contextData = String.join(", \n", list);

        log.info("contextData: {}", contextData);

        return chatClient
                .prompt(q)
                .advisors(advisorSpec -> {
                    advisorSpec.param(ChatMemory.CONVERSATION_ID,userId);
                })
                .advisors(new SimpleLoggerAdvisor())
                .system(system->system.text(systemMessage)
                        .param("documents",contextData))
                .user(user->user.text(userMessage)
                        .param("query",q))
                .call()
                .content();

    }

    public Flux<String> streamChat(String q) {
        return chatClient
                .prompt(q)
                .system(system->system.text(systemMessage))
                .user(user->user.text(userMessage))
                .stream()
                .content();
    }

    public void saveData(List<String> list) {

        List<Document> documentList = list.stream().map(Document::new).toList();
        this.vectorStore.add(documentList);


    }
}
