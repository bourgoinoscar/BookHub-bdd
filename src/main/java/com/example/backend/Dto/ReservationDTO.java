package com.example.backend.Dto;

public record ReservationDTO(Integer id,
                             String statut,
                             Integer utilisateurId,
                             Integer livreId) {
}
