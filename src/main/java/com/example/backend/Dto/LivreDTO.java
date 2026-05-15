package com.example.backend.Dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LivreDTO(@Schema(accessMode = Schema.AccessMode.READ_ONLY) Integer id,
                       @Schema(description = "Titre du livre", example = "The Legend of Zelda : souvenirs d'enfance") String titre,
                       @Schema(description = "Nom de l'auteur", example = "Matthieu Meriot") String auteur,
                       @Schema(description = "Catégorie du livre", example = "Drame")String categorie,
                       @Schema(description = "Résumé du livre", example = "Dans ce livre, je mélange récit de vie avec le monde du jeu vidéo, plus précisément celui de The Legend of Zelda avec en bonus, quelques images faisant penser à l'univers de Zelda.") String resume,
                       @Schema(description = "ISBN du livre", example = "2322422878") String isbn,
                       @Schema(description = "Nombre de livres disponible", example = "5") int quantite) {
}
