package com.example.backend.Dto;

import java.time.LocalDate;

public record UtilisateurDTO(Integer id,
                             String nom,
                             String prenom,
                             String email,
                             String password,
                             LocalDate dateDeNaissance,
                             String tel,
                             Integer roleId,
                             String roleNom) {
}
