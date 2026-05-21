package com.example.backend.Mapper;

import com.example.backend.Dto.ReservationDTO;
import com.example.backend.Entity.Reservation;

public class ReservationMapper {
    public static ReservationDTO toDTO(Reservation entity) {
        if (entity == null) return null;

        return new ReservationDTO(
                entity.getId(), //
                entity.getStatut() != null ? entity.getStatut().name() : null, //
                entity.getUtilisateur() != null ? entity.getUtilisateur().getId() : null, //
                entity.getLivre() != null ? entity.getLivre().getId() : null //
        );
    }
}
