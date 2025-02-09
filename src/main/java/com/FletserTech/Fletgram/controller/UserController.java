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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

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

    @Operation(summary = "Cria um novo usuário")
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            User user = userService.createUser(userDTO);
            return ResponseEntity.ok(user);
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
        Optional<User> userOptional = userService.findByEmail(loginDTO.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (BCrypt.checkpw(loginDTO.getPassword(), user.getPasswordHash())) {
                // Geração do token JWT
                Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

                String token = Jwts.builder()
                        .setSubject(user.getUsername()) // Informações do usuário no token
                        .setIssuedAt(new Date()) // Data de emissão
                        .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Token expira em 1 hora
                        .signWith(key, SignatureAlgorithm.HS256) // Assinatura segura
                        .compact();

                return ResponseEntity.ok(new TokenResponse(token));
            } else {
                return ResponseEntity.status(401).body("Senha incorreta");
            }
        } else {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
    }

    @Operation(summary = "Deleta um usuário pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
