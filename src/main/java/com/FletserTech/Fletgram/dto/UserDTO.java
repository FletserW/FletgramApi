package com.FletserTech.Fletgram.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    
    private String fullName;

    @NotBlank(message = "Nome de usuário é obrigatório")
    @Size(min = 3, max = 50)
    private String username;

    @Email(message = "E-mail inválido")
    private String email;

    private String phone;

    @NotBlank(message = "Senha é obrigatória")
    private String password;

    private String bio;
}
