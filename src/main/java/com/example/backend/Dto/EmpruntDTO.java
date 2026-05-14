package com.example.backend.Dto;

import java.time.LocalDate;

public record EmpruntDTO(Integer id,
                         LocalDate dateEmprunt,
                         LocalDate dateRetourPrevu,
                         LocalDate dateRetourEffectif,
                         Integer utilisateurId,
                         String nomUtilisateur,
                         Integer livreId,
                         String titreLivre) {
}
