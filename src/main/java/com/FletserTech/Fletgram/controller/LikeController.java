package com.FletserTech.Fletgram.controller;

import com.FletserTech.Fletgram.dto.LikeDTO;
import com.FletserTech.Fletgram.service.LikeService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @Operation(summary = "Curti um post")
    @PostMapping("/{userId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long userId, @RequestBody LikeDTO likeDTO) {
        String response = likeService.likePost(userId, likeDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Descurtir um post")
    @DeleteMapping("/{userId}/unlike/{postId}")
    public ResponseEntity<String> unlikePost(@PathVariable Long userId, @PathVariable Long postId) {
        String response = likeService.unlikePost(userId, postId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retorna o numero de curtidas do post")
    @GetMapping("/count/{postId}")
    public ResponseEntity<Long> countLikes(@PathVariable Long postId) {
        long count = likeService.countLikes(postId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Verifica se um usuário curtiu um post")
    @GetMapping("/{userId}/check/{postId}")
    public ResponseEntity<Boolean> hasUserLikedPost(@PathVariable Long userId, @PathVariable Long postId) {
        boolean hasLiked = likeService.hasUserLikedPost(userId, postId);
        return ResponseEntity.ok(hasLiked);
    }
    

}