package com.FletserTech.Fletgram.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowerDTO {
    private Long userId;
    private Long followerId;
}
