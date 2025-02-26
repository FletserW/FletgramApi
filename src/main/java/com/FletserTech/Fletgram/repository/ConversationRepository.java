package com.FletserTech.Fletgram.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.FletserTech.Fletgram.model.Conversation;
import com.FletserTech.Fletgram.model.ConversationParticipant;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    
    // Método para buscar uma conversa entre dois usuários
    @Query("SELECT c FROM Conversation c JOIN c.participants p1 JOIN c.participants p2 " +
           "WHERE p1.user.id = :userId1 AND p2.user.id = :userId2 " +
           "AND p1.user.id != p2.user.id")
    Conversation findByUserIds(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    // Método para buscar conversas de um usuário específico
    @Query("SELECT c FROM Conversation c JOIN c.participants p " +
           "WHERE p.user.id = :userId")
    List<Conversation> findByUserId(@Param("userId") Long userId);
}



