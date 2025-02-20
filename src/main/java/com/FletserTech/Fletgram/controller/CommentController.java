package com.FletserTech.Fletgram.controller;

import com.FletserTech.Fletgram.dto.CommentDTO;
import com.FletserTech.Fletgram.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/post/{postId}")
    public List<CommentDTO> getCommentsByPost(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @PostMapping
    public CommentDTO addComment(@RequestBody CommentDTO commentDTO) {
        return commentService.addComment(commentDTO);
    }
}

