package com.FletserTech.Fletgram.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.FletserTech.Fletgram.model.Conversation;
import com.FletserTech.Fletgram.model.ConversationParticipant;
import com.FletserTech.Fletgram.model.User;
import com.FletserTech.Fletgram.repository.ConversationParticipantRepository;
import com.FletserTech.Fletgram.repository.ConversationRepository;
import com.FletserTech.Fletgram.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
        // Verificar se a lista de IDs de usuários contém duplicatas
        Set<Long> uniqueUserIds = new HashSet<>(userIds);
        if (uniqueUserIds.size() != userIds.size()) {
            throw new RuntimeException("Não é possível criar uma conversa com o mesmo usuário duas vezes.");
        }

        // Verificar se a lista de IDs não contém apenas um usuário
        if (userIds.size() == 1) {
            throw new RuntimeException("Não é possível criar uma conversa com apenas um usuário.");
        }

        // Ordena os IDs dos usuários para garantir que a ordem não importe
        userIds.sort(Long::compareTo);  // Ordena os IDs em ordem crescente

        // Se a conversa não for um grupo, verifica se já existe uma conversa entre os usuários
        if (userIds.size() == 2) {
            Conversation existingConversation = getExistingConversation(userIds);
            if (existingConversation != null) {
                // Se já existir uma conversa, retorna a conversa existente
                return existingConversation;
            }
        }

        // Cria uma nova conversa, se não existir uma
        Conversation conversation = new Conversation();
        conversation.setIsGroup(userIds.size() > 2);  // Se houver mais de 2 usuários, é um grupo
        conversation.setName(conversation.isGroup() ? "Novo Grupo" : null);  // Nome do grupo (apenas para grupos)

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

    public Conversation getExistingConversation(List<Long> userIds) {
        // Ordena os IDs para garantir que a ordem não importe
        userIds.sort(Long::compareTo);

        // Aqui passamos os dois primeiros IDs ordenados para o repositório
        return conversationRepository.findByUserIds(userIds.get(0), userIds.get(1));
    }

    // Método para buscar todas as conversas de um usuário
    public List<Conversation> getConversationsForUser(Long userId) {
        return conversationRepository.findByUserId(userId);  // Agora usa o novo método do repositório
    }

    // Método para buscar a quantidade de participantes de uma conversa
    public long getParticipantCount(Long conversationId) {
        return conversationParticipantRepository.countByConversationId(conversationId);
    }
}
