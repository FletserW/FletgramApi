package com.FletserTech.Fletgram.service;

import com.FletserTech.Fletgram.dto.LikeDTO;
import com.FletserTech.Fletgram.model.Like;
import com.FletserTech.Fletgram.model.Post;
import com.FletserTech.Fletgram.model.User;
import com.FletserTech.Fletgram.repository.LikeRepository;
import com.FletserTech.Fletgram.repository.PostRepository;
import com.FletserTech.Fletgram.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public String likePost(Long userId, LikeDTO likeDTO) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Post> post = postRepository.findById(likeDTO.getPostId());

        if (user.isEmpty() || post.isEmpty()) {
            throw new RuntimeException("Usuário ou post não encontrado");
        }

        if (likeRepository.existsByUserAndPost(user.get(), post.get())) {
            return "Post já foi curtido pelo usuário";
        }
        
        

        Like like = new Like();
        like.setUser(user.get());
        like.setPost(post.get());

        likeRepository.save(like);
        return "Post curtido com sucesso";
    }

    @Transactional
    public String unlikePost(Long userId, Long postId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Post> post = postRepository.findById(postId);

        if (user.isEmpty() || post.isEmpty()) {
            throw new RuntimeException("Usuário ou post não encontrado");
        }

        if (!likeRepository.existsByUserAndPost(user.get(), post.get())) {
            return "O usuário não curtiu este post";
        }

        likeRepository.deleteByUserAndPost(user.get(), post.get());
        return "Like removido com sucesso";
    }

    public long countLikes(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new RuntimeException("Post não encontrado");
        }
        return likeRepository.countByPost(post.get());
    }

    public boolean hasUserLikedPost(Long userId, Long postId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Post> post = postRepository.findById(postId);
    
        if (user.isEmpty() || post.isEmpty()) {
            throw new RuntimeException("Usuário ou post não encontrado");
        }
    
        return likeRepository.existsByUserAndPost(user.get(), post.get());
    }
    
}