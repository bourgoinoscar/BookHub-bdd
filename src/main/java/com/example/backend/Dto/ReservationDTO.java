package com.example.backend.Dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReservationDTO(@Schema(accessMode = Schema.AccessMode.READ_ONLY) Integer id,
                             @Schema(description = "Statut de la résevation", example = "DISPONIBLE")String statut,
                             @Schema(accessMode = Schema.AccessMode.READ_ONLY) Integer utilisateurId,
                             @Schema(accessMode = Schema.AccessMode.READ_ONLY)Integer livreId) {
}
