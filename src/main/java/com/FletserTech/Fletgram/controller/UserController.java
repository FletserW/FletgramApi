package com.FletserTech.Fletgram.controller;

import com.FletserTech.Fletgram.dto.LoginDTO;
import com.FletserTech.Fletgram.dto.TokenResponse;
import com.FletserTech.Fletgram.dto.UserDTO;
import com.FletserTech.Fletgram.model.User;
import com.FletserTech.Fletgram.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String UPLOAD_DIR = "uploads/";
    

    @Value("${jwt.secret}") // Pegando a chave secreta do application.properties
    private String secretKey;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Lista todos os usuários")
    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @Operation(summary = "Atualiza os dados de um usuário existente")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            // Atualizando os dados do usuário
            User updatedUser = userService.updateUser(id, userDTO);

            if (updatedUser != null) {
                // Retorna os dados do usuário atualizado
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }


    @Operation(summary = "Cria um novo usuário")
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            User user = userService.createUser(userDTO);
            
            // Retornando status 200 e o ID do usuário
            return ResponseEntity.ok(Collections.singletonMap("id", user.getId()));


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar usuário: " + e.getMessage());
        }
    }


    @Operation(summary = "Busca um usuário pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/login")
    @Operation(summary = "Rota responsável por autenticar um usuário com JWT")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        logger.info("Iniciando processo de login para o email: {}", loginDTO.getEmail());

        // Verifica se o usuário existe
        Optional<User> userOptional = userService.findByEmail(loginDTO.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            logger.info("Usuário encontrado: {}", user.getUsername());

            // Comparação direta da senha fornecida com a armazenada
            if (loginDTO.getPassword().equals(user.getPasswordHash())) {
                logger.info("Senha correta para o usuário: {}", user.getUsername());

                // Geração do token JWT
                try {
                    Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
                    String token = Jwts.builder()
                            .setSubject(user.getUsername())
                            .setIssuedAt(new Date())
                            .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Expira em 1 hora
                            .signWith(key, SignatureAlgorithm.HS256)
                            .compact();

                    logger.info("Token gerado com sucesso para o usuário: {}", user.getUsername());

                    // Criando a resposta com o token, username e id
                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("username", user.getUsername()); // Enviar o nome de usuário real
                    response.put("id", user.getId()); // Se quiser o ID também

                    return ResponseEntity.ok(response); // Retorna a resposta com o token e dados do usuário
                } catch (Exception e) {
                    logger.error("Erro ao gerar o token JWT para o usuário: {}", user.getUsername(), e);
                    return ResponseEntity.status(500).body("Erro interno ao gerar token");
                }
            } else {
                logger.warn("Senha incorreta fornecida para o usuário: {}", loginDTO.getEmail());
                return ResponseEntity.status(401).body("Senha incorreta");
            }
        } else {
            logger.warn("Usuário não encontrado para o email: {}", loginDTO.getEmail());
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
    }

    @PostMapping("/{id}/uploadProfilePicture")
public ResponseEntity<Map<String, String>> uploadProfilePicture(
        @PathVariable Long id,
        @RequestParam("file") MultipartFile file) {
    try {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuário não encontrado"));
        }

        User user = userOptional.get();

        if (file.isEmpty() || !file.getContentType().startsWith("image")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Arquivo inválido!"));
        }

        Files.createDirectories(Paths.get(UPLOAD_DIR));
        
        // Verifica se o usuário já possui uma imagem
        String existingFileName = user.getProfilePicture();
        String fileName;

        if (existingFileName != null) {
            // Se já tiver uma imagem, usa o nome da imagem atual
            fileName = existingFileName;
        } else {
            // Caso contrário, gera um novo nome
            fileName = "profile_" + id + "_" + System.currentTimeMillis() + ".jpg";
        }

        // Define o caminho do arquivo
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);

        // Substitui o arquivo existente (se houver) ou cria um novo
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Atualiza o nome da imagem do usuário no banco de dados
        user.setProfilePicture(fileName);
        userService.updateUserProfilePicture(id, fileName);

        String imageUrl = "http://192.168.0.5:8082/uploads/" + fileName;
        return ResponseEntity.ok(Map.of("profile_picture", imageUrl));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Erro ao salvar a imagem"));
    }
}


    @GetMapping("/uploads/{fileName:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/profilePicture")
    public ResponseEntity<Map<String, String>> getProfilePicture(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isEmpty() || userOptional.get().getProfilePicture() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = userOptional.get();
        String imageUrl = "http://192.168.0.5:8082/uploads/" + user.getProfilePicture();
        return ResponseEntity.ok(Map.of("profile_picture", imageUrl));
    }

    @Operation(summary = "Deleta um usuário pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
