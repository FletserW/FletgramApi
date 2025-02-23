package com.FletserTech.Fletgram.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.FletserTech.Fletgram.model.Conversation;
import com.FletserTech.Fletgram.model.ConversationParticipant;
import com.FletserTech.Fletgram.model.User;
import com.FletserTech.Fletgram.repository.ConversationParticipantRepository;
import com.FletserTech.Fletgram.repository.ConversationRepository;
import com.FletserTech.Fletgram.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;

    public ConversationService(ConversationRepository conversationRepository, 
                               UserRepository userRepository, 
                               ConversationParticipantRepository conversationParticipantRepository) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.conversationParticipantRepository = conversationParticipantRepository;
    }

    @Transactional
    public Conversation createConversation(List<Long> userIds) {
        // Cria uma nova conversa
        Conversation conversation = new Conversation();
        
        // Define se a conversa é um grupo ou não (se tiver mais de 2 participantes, é um grupo)
        conversation.setIsGroup(userIds.size() > 2);  // Se houver mais de 2 usuários, é um grupo
        conversation.setName(conversation.isGroup() ? "Novo Grupo" : null);  // Nome do grupo (apenas para grupo)

        // Salva a conversa no banco de dados
        conversation = conversationRepository.save(conversation);

        // Adiciona os participantes à conversa
        for (Long userId : userIds) {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                ConversationParticipant participant = new ConversationParticipant();
                participant.setConversation(conversation);  // Associação com a conversa
                participant.setUser(user);  // Associação com o usuário
                conversationParticipantRepository.save(participant);  // Salva o participante no banco
            } else {
                throw new RuntimeException("Usuário não encontrado com ID: " + userId);
            }
        }

        return conversation;  // Retorna a conversa criada
    }

    // Método para buscar todas as conversas de um usuário
    public List<Conversation> getConversationsForUser(Long userId) {
        List<Conversation> conversations = conversationRepository.findByUserId(userId);
        
        for (Conversation conversation : conversations) {
            if (!conversation.isGroup()) {
                // Encontrar o outro participante (que não é o usuário atual)
                Optional<ConversationParticipant> participant = conversationParticipantRepository
                        .findByConversationIdAndUserIdNot(conversation.getId(), userId);
                
                // Se encontramos o outro participante, definimos o nome da conversa com o username dele
                if (participant.isPresent()) {
                    User otherUser = participant.get().getUser();
                    conversation.setName(otherUser.getUsername());  // Atualizando o nome da conversa
                }
            }
        }
    
        return conversations;
    }

    
}    
    
