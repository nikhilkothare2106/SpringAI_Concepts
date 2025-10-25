package com.ai.controller;

import com.ai.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping
public class ChatController {

    private final ChatService chatService;
    public ChatController(ChatService chatService) {
       this.chatService = chatService;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam String q){
        String chat = chatService.chat(q);
        return ResponseEntity.ok(chat);
    }


    @GetMapping("/stream")
    public ResponseEntity<Flux<String>> streamChat(@RequestParam String q){
        Flux<String> streamChat = chatService.streamChat(q);
        return ResponseEntity.ok(streamChat);
    }
}
