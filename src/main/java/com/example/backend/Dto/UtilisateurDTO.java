package com.example.backend.Dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record UtilisateurDTO(@Schema(accessMode = Schema.AccessMode.READ_ONLY) Integer id,
                             @Schema(description = "Nom de l'utilisateur", example = "Terrieur") String nom,
                             @Schema(description = "Prénom de l'utilisateur", example = "Alex")String prenom,
                             @Schema(description = "Email de l'utilisateur", example = "alexterrieur@gmail.com") String email,
                             @Schema(description = "Mot de passe de l'utilisateur", example = "JeSuisTropFort") String password,
                             @Schema(description = "Date de naissance de l'utilisateur", example = "1997-01-10") LocalDate dateDeNaissance,
                             @Schema(description = "Numéro de téléphone de l'utilisateur", example = "0604010101") String tel,
                             @Schema(accessMode = Schema.AccessMode.READ_ONLY) Integer roleId,
                             @Schema(accessMode = Schema.AccessMode.READ_ONLY) String roleNom) {
}
