package com.FletserTech.Fletgram.service;

import com.FletserTech.Fletgram.dto.CommentDTO;
import com.FletserTech.Fletgram.model.Comment;
import com.FletserTech.Fletgram.repository.CommentRepository;
import com.FletserTech.Fletgram.repository.UserRepository;
import com.FletserTech.Fletgram.repository.PostRepository;
import com.FletserTech.Fletgram.model.User;
import com.FletserTech.Fletgram.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository; // Adicionado
    private final PostRepository postRepository; // Adicionado

    public CommentDTO addComment(CommentDTO dto) {
        // Busca o usuário e o post no banco de dados
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        // Cria o comentário e associa os relacionamentos
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setUser(user);
        comment.setPost(post);

        // Salva no banco de dados
        comment = commentRepository.save(comment);

        // Retorna o DTO atualizado
        CommentDTO savedComment = new CommentDTO();
        savedComment.setId(comment.getId());
        savedComment.setUserId(comment.getUser().getId());
        savedComment.setPostId(comment.getPost().getId());
        savedComment.setContent(comment.getContent());

        return savedComment;
    }
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(comment -> {
                    CommentDTO dto = new CommentDTO();
                    dto.setId(comment.getId());
                    dto.setUserId(comment.getUser().getId());
                    dto.setPostId(comment.getPost().getId());
                    dto.setContent(comment.getContent());
                    dto.setCreated_at(comment.getCreated_at());
                    return dto;
                }).collect(Collectors.toList());
    }
}