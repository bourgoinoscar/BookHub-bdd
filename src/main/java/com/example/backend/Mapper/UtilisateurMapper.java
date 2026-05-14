package com.example.backend.Mapper;

import com.example.backend.Dto.RoleDTO;
import com.example.backend.Dto.UtilisateurDTO;
import com.example.backend.Entity.Utilisateur;

public class UtilisateurMapper {

    public static UtilisateurDTO toDTO(Utilisateur entity) {
        if (entity == null) return null;

        RoleDTO roleDTO = entity.getRole() != null ?
                new RoleDTO(entity.getRole().getId(), entity.getRole().getNom()) : null;

        return new UtilisateurDTO(
                entity.getId(),
                entity.getNom(),
                entity.getPrenom(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getDateNaissance(),
                entity.getTel(),
                roleDTO.id(),
                roleDTO.nom()
        );
    }
}
