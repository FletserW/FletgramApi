package com.FletserTech.Fletgram.repository;

import com.FletserTech.Fletgram.model.Follower;
import com.FletserTech.Fletgram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
    boolean existsByUserAndFollower(User user, User follower);
    void deleteByUserAndFollower(User user, User follower);
    List<Follower> findByUser(User user);
    List<Follower> findByFollower(User follower);
}