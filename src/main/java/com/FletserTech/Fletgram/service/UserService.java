package com.FletserTech.Fletgram.service;

import com.FletserTech.Fletgram.dto.UserDTO;
import com.FletserTech.Fletgram.model.User;
import com.FletserTech.Fletgram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
        user.setDate_of_birth(userDTO.getDate_of_birth());
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserDTO userDTO) {
        // Buscar o usuário existente pelo ID
        Optional<User> optionalUser = userRepository.findById(id);
        
        if (!optionalUser.isPresent()) {
            return null;  // Caso o usuário não seja encontrado
        }

        User user = optionalUser.get();

        // Atualizando os dados do usuário
        if (userDTO.getFullName() != null) user.setFullName(userDTO.getFullName());
        if (userDTO.getUsername() != null) user.setUsername(userDTO.getUsername());
        if (userDTO.getEmail() != null) user.setEmail(userDTO.getEmail());
        if (userDTO.getPhone() != null) user.setPhone(userDTO.getPhone());
        if (userDTO.getPassword() != null) user.setPasswordHash(BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt())); // Se for necessário atualizar a senha
        
        // Atualizando os novos campos (bio, pronomes, gênero e links)
        if (userDTO.getBio() != null) user.setBio(userDTO.getBio());
        if (userDTO.getDate_of_birth() != null) user.setDate_of_birth(userDTO.getDate_of_birth());
        if (userDTO.getPronouns() != null) user.setPronouns(userDTO.getPronouns());
        if (userDTO.getGender() != null) user.setGender(userDTO.getGender());
        if (userDTO.getLinks() != null) user.setLinks(userDTO.getLinks());
        user.setUpdatedAt(LocalDateTime.now());
        
        // Salvando as alterações no banco de dados
        return userRepository.save(user);
    }

    public void updateUserProfilePicture(Long id, String fileName) {
        Optional<User> userOptional = userRepository.findById(id); // Agora usa userRepository
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setProfilePicture(fileName);  // Atualiza o campo profilePicture com o nome do novo arquivo
            userRepository.save(user); // Salva o usuário atualizado no banco de dados
        }
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
