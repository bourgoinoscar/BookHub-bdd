package com.example.backend.Dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record RevueDTO(
        @Schema(description = "Note entre 0 et 5", example = "3") int note,
        @Schema(description = "Commentaire de la review", example = "Incroyable Mr Pot de beurre") String commentaire,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) LocalDate datePublication,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) Integer utilisateurId,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String utilisateurNom,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) Integer livreId,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String livreTitre) {
}
