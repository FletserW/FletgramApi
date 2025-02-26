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
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Criar uma nova conversa", description = "Cria uma nova conversa com base nos IDs dos usuários fornecidos.")
    @PostMapping("/")
    public ResponseEntity<Conversation> createConversation(@RequestBody List<Long> userIds) {
        return ResponseEntity.ok(conversationService.createConversation(userIds));  // Chama o método correto do serviço
    }

    @Operation(summary = "Obter conversas de um usuário", description = "Retorna uma lista de conversas do usuário especificado.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Conversation>> getConversationsByUser(@PathVariable Long userId) {
        List<Conversation> conversations = conversationService.getConversationsForUser(userId);
        return ResponseEntity.ok(conversations);
    }

    @Operation(summary = "Obter número de participantes", description = "Retorna o número de participantes em uma conversa especificada.")
    @GetMapping("/{conversationId}/participantCount")
    public ResponseEntity<Long> getParticipantCount(@PathVariable Long conversationId) {
        long participantCount = conversationService.getParticipantCount(conversationId);
        return ResponseEntity.ok(participantCount);
    }
}
