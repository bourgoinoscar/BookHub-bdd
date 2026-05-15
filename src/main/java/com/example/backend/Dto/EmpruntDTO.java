package com.example.backend.Dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record EmpruntDTO( @Schema(accessMode = Schema.AccessMode.READ_ONLY) Integer id,
                          @Schema(accessMode = Schema.AccessMode.READ_ONLY) LocalDate dateEmprunt,
                          @Schema(accessMode = Schema.AccessMode.READ_ONLY) LocalDate dateRetourPrevu,
                          @Schema(accessMode = Schema.AccessMode.READ_ONLY) LocalDate dateRetourEffectif,
                          @Schema(accessMode = Schema.AccessMode.READ_ONLY) Integer utilisateurId,
                          @Schema(accessMode = Schema.AccessMode.READ_ONLY) String nomUtilisateur,
                          @Schema(accessMode = Schema.AccessMode.READ_ONLY) Integer livreId,
                          @Schema(accessMode = Schema.AccessMode.READ_ONLY) String titreLivre) {
}
