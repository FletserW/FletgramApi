package com.FletserTech.Fletgram.service;

import com.FletserTech.Fletgram.dto.PostDTO;
import com.FletserTech.Fletgram.model.Post;
import com.FletserTech.Fletgram.model.User;
import com.FletserTech.Fletgram.repository.PostRepository;
import com.FletserTech.Fletgram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public PostDTO createPost(Long userId, String content) {
        // Encontra o usuário pelo ID
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    
        // Cria o novo post
        Post post = new Post();
        post.setUser(user);
        post.setContent(content);  // O content agora armazenará a URL da imagem
    
        // Salva o post no banco de dados
        Post savedPost = postRepository.save(post);
    
        // Retorna o DTO com os dados do post
        return new PostDTO(savedPost.getId(), userId, savedPost.getContent(), savedPost.getCreatedAt());
    }
    
    public List<PostDTO> getUserPosts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return postRepository.findByUser(user).stream()
                .map(post -> new PostDTO(post.getId(), userId, post.getContent(), post.getCreatedAt()))
                .collect(Collectors.toList());
    }


    public List<PostDTO> getAllPosts() {
        // Busca todos os posts no banco de dados
        List<Post> posts = postRepository.findAll();
        
        // Converte os posts para o formato DTO
        return posts.stream()
                    .map(post -> new PostDTO(post.getId(), post.getUser().getId(), post.getContent(), post.getCreatedAt()))
                    .collect(Collectors.toList());
    }

    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post não encontrado"));
        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("Você não tem permissão para deletar este post");
        }
        postRepository.delete(post);
    }
}

