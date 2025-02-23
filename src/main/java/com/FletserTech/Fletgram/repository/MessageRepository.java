package com.FletserTech.Fletgram.repository;

import com.FletserTech.Fletgram.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationId(Long conversationId);

}

