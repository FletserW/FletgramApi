package com.FletserTech.Fletgram.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FletserTech.Fletgram.model.Conversation;
import com.FletserTech.Fletgram.service.ConversationService;  // Import the service

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/conversations")
public class ConversationController {
    
    private final ConversationService conversationService;  // Correct the injected service

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;  // Use the correct service
    }

    @PostMapping("/")
    public ResponseEntity<Conversation> createConversation(@RequestBody List<Long> userIds) {
        return ResponseEntity.ok(conversationService.createConversation(userIds));  // Call the correct service method
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Conversation>> getConversationsByUser(@PathVariable Long userId) {
        List<Conversation> conversations = conversationService.getConversationsForUser(userId);
        return ResponseEntity.ok(conversations);
    }

}
