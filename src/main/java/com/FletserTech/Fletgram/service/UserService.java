package com.FletserTech.Fletgram.service;

import com.FletserTech.Fletgram.dto.UserDTO;
import com.FletserTech.Fletgram.model.User;
import com.FletserTech.Fletgram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //@Autowired
    //private BCryptPasswordEncoder passwordEncoder;

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        //user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
        user.setPasswordHash(userDTO.getPassword());
        user.setBio(userDTO.getBio());
        return userRepository.save(user);
    }

    public void updateUser(User user) {
    user.setUpdatedAt(LocalDateTime.now());
    userRepository.save(user);
}


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByEmail(String email) {
        System.out.println("Email recebido no login: " + email);
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}