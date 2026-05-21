package com.example.backend.Mapper;

import com.example.backend.Dto.RoleDTO;
import com.example.backend.Entity.Role;

public class RoleMapper {
    public static RoleDTO toDTO(Role entity) {
        if (entity == null) return null;
        return new RoleDTO(
                entity.getId(), //
                entity.getNom()  //
        );
    }

    public static Role toEntity(RoleDTO dto) {
        if (dto == null) return null;
        Role entity = new Role();
        entity.setId(dto.id());
        entity.setNom(dto.nom());
        return entity;
    }

}
