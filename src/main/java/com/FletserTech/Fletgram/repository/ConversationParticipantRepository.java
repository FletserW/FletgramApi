package com.FletserTech.Fletgram.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.FletserTech.Fletgram.model.ConversationParticipant;

public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, Long> {

    // Método para encontrar participantes de uma conversa
    List<ConversationParticipant> findByUserId(Long userId);
    
    // Novo método para contar o número de participantes por conversa
    long countByConversationId(Long conversationId);

    // Busca os participantes da conversa, excluindo o usuário com userId
    Optional<ConversationParticipant> findByConversationIdAndUserIdNot(Long conversationId, Long userId);
}



