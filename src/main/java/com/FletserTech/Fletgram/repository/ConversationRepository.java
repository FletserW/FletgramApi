package com.FletserTech.Fletgram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.FletserTech.Fletgram.model.Conversation;
import com.FletserTech.Fletgram.model.ConversationParticipant;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("SELECT c FROM Conversation c JOIN c.participants p WHERE p.user.id = :userId")
    List<Conversation> findByUserId(@Param("userId") Long userId);
}
