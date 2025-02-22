package com.FletserTech.Fletgram.service;

import com.FletserTech.Fletgram.dto.PostDTO;
import com.FletserTech.Fletgram.model.Post;
import com.FletserTech.Fletgram.model.PostImage;
import com.FletserTech.Fletgram.model.User;
import com.FletserTech.Fletgram.repository.PostImageRepository;
import com.FletserTech.Fletgram.repository.PostRepository;
import com.FletserTech.Fletgram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

     @Autowired
    private PostImageRepository postImageRepository;

    @Value("${server.base-url}") // Defina no application.properties
    private String baseUrl;

    public PostDTO createPost(Long userId, MultipartFile[] files) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    
        // Criação do post
        Post post = new Post();
        post.setUser(user);
        post.setContent("Conteúdo do Post");
    
        // Salvar o post antes de salvar as imagens
        Post savedPost = postRepository.save(post);
    
        List<PostImage> postImages = new ArrayList<>();
        for (MultipartFile file : files) {
            String imageUrl = uploadFile(file);
            
            PostImage postImage = new PostImage();
            postImage.setPost(savedPost);
            postImage.setImage_url(imageUrl);
            postImages.add(postImage);
        }
    
        postImageRepository.saveAll(postImages); // Salva todas as imagens associadas ao post
    
        return new PostDTO(savedPost.getId(), userId, postImages.stream().map(PostImage::getImage_url).collect(Collectors.toList()), savedPost.getCreatedAt());
    }

    private String uploadFile(MultipartFile file) {
        try {
            Path uploadDir = Paths.get("uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String fileName = file.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return baseUrl + "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao fazer upload do arquivo", e);
        }
    }

    
    public List<PostDTO> getUserPosts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        return postRepository.findByUser(user).stream()
                .map(post -> new PostDTO(
                        post.getId(),
                        userId,
                        postImageRepository.findByPostId(post.getId()).stream()
                                .map(PostImage::getImage_url)
                                .collect(Collectors.toList()),
                        post.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
    


    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        
        return posts.stream().map(post -> 
            new PostDTO(
                post.getId(),
                post.getUser().getId(),
                postImageRepository.findByPostId(post.getId()).stream().map(PostImage::getImage_url).toList(),
                post.getCreatedAt()
            )
        ).collect(Collectors.toList());
    }

    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post não encontrado"));
        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("Você não tem permissão para deletar este post");
        }
        postRepository.delete(post);
    }
}

