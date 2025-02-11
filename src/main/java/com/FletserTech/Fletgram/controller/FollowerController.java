package com.FletserTech.Fletgram.controller;

import com.FletserTech.Fletgram.dto.FollowerDTO;
import com.FletserTech.Fletgram.service.FollowerService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/followers")
public class FollowerController {

    private final FollowerService followerService;

    public FollowerController(FollowerService followerService) {
        this.followerService = followerService;
    }

    @Operation(summary = "Rota para seguir usuario")
    @PostMapping("/follow")
    public ResponseEntity<String> followUser(@RequestBody FollowerDTO followerDTO) {
        followerService.followUser(followerDTO);
        return ResponseEntity.ok("Seguiu com sucesso!");
    }
    
    @Operation(summary = "Rota para deixar de seguir usuario")
    @PostMapping("/unfollow")
    public ResponseEntity<String> unfollowUser(@RequestBody FollowerDTO followerDTO) {
        followerService.unfollowUser(followerDTO);
        return ResponseEntity.ok("Deixou de seguir com sucesso!");
    }

    @Operation(summary = "Rota retorna a lista de seguidores do usuario")
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<FollowerDTO>> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(followerService.getFollowers(userId));
    }

    @Operation(summary = "Rota que retorna a lista de seguidores que o usuario segue")
    @GetMapping("/{followerId}/following")
    public ResponseEntity<List<FollowerDTO>> getFollowing(@PathVariable Long followerId) {
        return ResponseEntity.ok(followerService.getFollowing(followerId));
    }
}