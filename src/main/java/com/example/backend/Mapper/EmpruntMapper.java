package com.example.backend.Mapper;

import com.example.backend.Dto.EmpruntDTO;
import com.example.backend.Entity.Emprunt;

public class EmpruntMapper {
    public static EmpruntDTO toDTO(Emprunt entity) {
        if (entity == null) return null;
        return new EmpruntDTO(
                entity.getId(),
                entity.getDateEmprunt(),
                entity.getDateRetourPrevu(),
                entity.getDateRetourEffectif(),
                entity.getUtilisateur() != null ? entity.getUtilisateur().getId() : null,
                entity.getUtilisateur() != null ? entity.getUtilisateur().getNom() : null,
                entity.getLivre() != null ? entity.getLivre().getId() : null,
                entity.getLivre() != null ? entity.getLivre().getTitre() : null
        );
    }
}
