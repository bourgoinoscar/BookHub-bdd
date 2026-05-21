package com.example.backend.Mapper;

import com.example.backend.Dto.LivreDTO;
import com.example.backend.Entity.Livre;

public class LivreMapper {
    public static LivreDTO toDTO(Livre entity) {
        if (entity == null) return null;
        return new LivreDTO(
                entity.getId(),
                entity.getTitre(),
                entity.getAuteur(),
                entity.getCategorie(),
                entity.getResume(),
                entity.getIsbn(),
                entity.getQuantite()
        );
    }
}
