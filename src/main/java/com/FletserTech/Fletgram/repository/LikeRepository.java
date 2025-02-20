package com.FletserTech.Fletgram.repository;

import com.FletserTech.Fletgram.model.Like;
import com.FletserTech.Fletgram.model.Post;
import com.FletserTech.Fletgram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);
    long countByPost(Post post);
    Like findByUserAndPost(User user, Post post);

    
}

