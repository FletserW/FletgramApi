package com.FletserTech.Fletgram.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDTO {
    
    private String fullName;

    @Size(min = 3, max = 50)
    private String username;

    @Email(message = "E-mail inválido")
    private String email;

    private String phone;

    
    private String password;

    private String bio;

    private String date_of_birth;

    private String profilePicture;
    private String pronouns;  
    private String gender;    
    private String links;     
}
