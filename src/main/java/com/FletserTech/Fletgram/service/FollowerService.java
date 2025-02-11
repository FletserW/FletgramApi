package com.FletserTech.Fletgram.service;


import com.FletserTech.Fletgram.dto.FollowerDTO;
import com.FletserTech.Fletgram.model.Follower;
import com.FletserTech.Fletgram.model.User;
import com.FletserTech.Fletgram.repository.FollowerRepository;
import com.FletserTech.Fletgram.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowerService {

    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;

    public FollowerService(FollowerRepository followerRepository, UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    public void followUser(FollowerDTO followerDTO) {
        User user = userRepository.findById(followerDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        User follower = userRepository.findById(followerDTO.getFollowerId())
                .orElseThrow(() -> new RuntimeException("Seguidor não encontrado"));

        if (followerRepository.existsByUserAndFollower(user, follower)) {
            throw new RuntimeException("Já está seguindo este usuário");
        }

        Follower newFollower = new Follower();
        newFollower.setUser(user);
        newFollower.setFollower(follower);
        followerRepository.save(newFollower);
    }

    @Transactional
    public void unfollowUser(FollowerDTO followerDTO) {
        User user = userRepository.findById(followerDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        User follower = userRepository.findById(followerDTO.getFollowerId())
                .orElseThrow(() -> new RuntimeException("Seguidor não encontrado"));

        followerRepository.deleteByUserAndFollower(user, follower);
    }

    public List<FollowerDTO> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return followerRepository.findByUser(user).stream()
                .map(f -> new FollowerDTO(f.getUser().getId(), f.getFollower().getId()))
                .collect(Collectors.toList());
    }

    public List<FollowerDTO> getFollowing(Long followerId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return followerRepository.findByFollower(follower).stream()
                .map(f -> new FollowerDTO(f.getUser().getId(), f.getFollower().getId()))
                .collect(Collectors.toList());
    }
}
