package com.example.backend.Dto;

import java.time.LocalDate;

public record RevueDTO(int note, String commentaire, LocalDate datePublication, Integer utilisateurId,
                       String utlisateurNom, Integer livreId, String livreTitre) {
}
