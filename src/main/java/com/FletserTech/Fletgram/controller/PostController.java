package com.FletserTech.Fletgram.controller;

import com.FletserTech.Fletgram.dto.PostDTO;
import com.FletserTech.Fletgram.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/posts")

public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/create")
public PostDTO createPost(@RequestParam Long userId, @RequestParam String content) {
    return postService.createPost(userId, content);
}


    @GetMapping("/user/{userId}")
    public List<PostDTO> getUserPosts(@PathVariable Long userId) {
        return postService.getUserPosts(userId);
    }

    @GetMapping("/all")
    public List<PostDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    @DeleteMapping("/{postId}")
    public String deletePost(@RequestParam Long userId, @PathVariable Long postId) {
        postService.deletePost(postId, userId);
        return "Post deletado com sucesso!";
    }
}

