package com.FletserTech.Fletgram.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private Long userId;
    private Long postId;
    private String content;
    private LocalDateTime created_at;
    
}
