package com.FletserTech.Fletgram.controller;

import com.FletserTech.Fletgram.dto.MessageDTO;
import com.FletserTech.Fletgram.model.Conversation;
import com.FletserTech.Fletgram.model.Message;
import com.FletserTech.Fletgram.model.User;
import com.FletserTech.Fletgram.service.MessageService;
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

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long conversationId) {
        return ResponseEntity.ok(messageService.getMessagesByConversation(conversationId));
    }

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

            String baseUrl = "https://electric-polished-perch.ngrok-free.app";
            String content = baseUrl + "/midia/" + fileName;

            Map<String, String> response = new HashMap<>();
            response.put("url", content);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("message", "Falha ao fazer upload."));
        }
    }
}
