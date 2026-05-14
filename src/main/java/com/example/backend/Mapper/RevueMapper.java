package com.example.backend.Mapper;

import com.example.backend.Dto.ReservationDTO;
import com.example.backend.Dto.RevueDTO;
import com.example.backend.Entity.Reservation;
import com.example.backend.Entity.Revue;

public class RevueMapper {
    public static RevueDTO toDTO(Revue entity) {
        if (entity == null) return null;

        return new RevueDTO(
                entity.getId(),
                entity.getCommentaire() != null ? entity.getCommentaire() : null,
                entity.getDatePublication() != null ? entity.getDatePublication() : null,
                entity.getUtilisateur() != null ? entity.getUtilisateur().getId() : null,
                entity.getUtilisateur() != null ? entity.getUtilisateur().getNom() : null,
                entity.getLivre() != null ? entity.getLivre().getId() : null,
                entity.getLivre() != null ? entity.getLivre().getTitre() : null
        );
    }
}
