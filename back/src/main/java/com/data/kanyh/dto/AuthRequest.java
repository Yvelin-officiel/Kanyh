package com.data.kanyh.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    private String userName;
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[#?!@$%^&*.\\-])[\\S]{10,20}$",
            message = "Le mot de passe doit contenir entre 10 et 20 caractères sans espaces, une majuscule, une minuscule, un chiffre et un caractère spécial"
    )
    private String password;

}
