package com.ai.controller;

import com.ai.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping
public class ChatController {

    private final ChatService chatService;
    public ChatController(ChatService chatService) {
       this.chatService = chatService;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(
            @RequestParam String q,
            @RequestHeader String userId){
        String chat = chatService.chat(q,userId);
        return ResponseEntity.ok(chat);
    }


    @GetMapping("/stream")
    public ResponseEntity<Flux<String>> streamChat(@RequestParam String q){
        Flux<String> streamChat = chatService.streamChat(q);
        return ResponseEntity.ok(streamChat);
    }
}
