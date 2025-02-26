package com.FletserTech.Fletgram.controller;

import com.FletserTech.Fletgram.dto.MessageDTO;
import com.FletserTech.Fletgram.model.Conversation;
import com.FletserTech.Fletgram.model.Message;
import com.FletserTech.Fletgram.model.User;
import com.FletserTech.Fletgram.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

   @Operation(summary = "Obter mensagens de uma conversa", description = "Retorna a lista de mensagens para a conversa especificada pelo ID.")
    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long conversationId) {
        return ResponseEntity.ok(messageService.getMessagesByConversation(conversationId));
    }

    @Operation(summary = "Enviar uma mensagem", description = "Envia uma nova mensagem para a conversa especificada, com dados do remetente, texto e mídia.")
    @PostMapping("/")
    public ResponseEntity<Message> sendMessage(@RequestBody MessageDTO messageDTO) {
        Message message = new Message();
        Conversation conversation = new Conversation();
        conversation.setId(messageDTO.getConversationId());

        User sender = new User();
        sender.setId(messageDTO.getSenderId());

        message.setConversation(conversation);
        message.setSender(sender);
        message.setMessageText(messageDTO.getMessageText());
        message.setMediaUrl(messageDTO.getMediaUrl());
        message.setMediaType(messageDTO.getMediaType());
        return ResponseEntity.ok(messageService.saveMessage(messageDTO));
    }

    @Operation(summary = "Fazer upload de mídia", description = "Faz o upload de um arquivo de mídia e retorna a URL acessível para o arquivo.")
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadMedia(@RequestParam("file") MultipartFile file) {
        try {
            Path uploadDir = Paths.get("midia");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String fileName = file.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String baseUrl = "https://fletgram.loca.lt";
            String content = baseUrl + "/midia/" + fileName;

            Map<String, String> response = new HashMap<>();
            response.put("url", content);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("message", "Falha ao fazer upload."));
        }
    }
}
