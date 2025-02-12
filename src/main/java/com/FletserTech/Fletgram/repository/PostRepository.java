package com.FletserTech.Fletgram.repository;

import com.FletserTech.Fletgram.model.Post;
import com.FletserTech.Fletgram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
}
