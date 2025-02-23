package com.FletserTech.Fletgram.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MessageDTO {
    private Long id;
    private Long conversationId;
    private Long senderId;
    private String messageText;
    private String mediaUrl;
    private String mediaType;
    private boolean edited;
    private boolean deleted;
    private LocalDateTime createdAt;
}