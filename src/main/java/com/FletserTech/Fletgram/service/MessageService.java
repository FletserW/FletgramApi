package com.FletserTech.Fletgram.service;

import com.FletserTech.Fletgram.dto.MessageDTO;
import com.FletserTech.Fletgram.model.Conversation;
import com.FletserTech.Fletgram.model.Message;
import com.FletserTech.Fletgram.model.User;

import com.FletserTech.Fletgram.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
private ConversationRepository conversationRepository;


    @Autowired
    private UserRepository userRepository;

    public Message saveMessage(MessageDTO messageDTO) {
        // Verifica se a conversa existe
        Conversation conversation = conversationRepository.findById(messageDTO.getConversationId())
    .orElseThrow(() -> new RuntimeException("Conversa não encontrada"));


        // Verifica se o remetente existe
        User sender = userRepository.findById(messageDTO.getSenderId())
                .orElseThrow(() -> new RuntimeException("Usuário remetente não encontrado"));

        // Criando a mensagem
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setMessageText(messageDTO.getMessageText());
        message.setMediaUrl(messageDTO.getMediaUrl());
        message.setMediaType(messageDTO.getMediaType());
        message.setDeleted(false);
        message.setEdited(false);
        message.setCreatedAt(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public List<Message> getMessagesByConversation(Long conversationId) {
        return messageRepository.findByConversationId(conversationId);
    }
    
}
 

