package com.FletserTech.Fletgram.repository;

import com.FletserTech.Fletgram.model.Comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
}
