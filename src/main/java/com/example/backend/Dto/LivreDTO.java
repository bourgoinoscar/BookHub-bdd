package com.example.backend.Dto;

public record LivreDTO(Integer id,
                       String titre,
                       String auteur,
                       String categorie,
                       String resume,
                       String isbn,
                       int quantite) {
}
