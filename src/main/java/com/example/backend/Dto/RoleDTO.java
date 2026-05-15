package com.example.backend.Dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record RoleDTO(@Schema(accessMode = Schema.AccessMode.READ_ONLY)Integer id, @Schema(description = "Nom du rôle", example = "ARCHIVISTE") String nom) {
}
