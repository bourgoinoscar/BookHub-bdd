package com.example.backend.Controller;

import com.example.backend.Dto.RoleDTO;
import com.example.backend.Entity.Role;
import com.example.backend.Service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Rôles", description = "Gestion des rôles")
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    //Comme demandé, seul l'admin peut créer de nouveaux rôles
    @Operation(summary = "Créer un nouveau rôle",
            description = "Permet de définir un nouveau rôle (ex: ARCHIVISTE). Réservé à l'ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rôle créé avec succès"),
            @ApiResponse(responseCode = "403", description = "Interdit : Seul l'ADMIN peut créer des rôles")
    })
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok(roleService.save(roleDTO));
    }

    //Seul l'admin peut voir la liste des rôles
    @Operation(summary = "Lister tous les rôles",
            description = "Récupère la liste de tous les rôles disponibles dans le système. Réservé à l'ADMIN.")
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }


    @Operation(summary = "Chercher un rôle par son nom",
            description = "Recherche exacte par libellé (ex: LECTEUR). Réservé à l'ADMIN.")
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/searchByName/{nom}") // searchByName est la pour différencier le getRoleByName et le getRoleByID
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoleDTO> getRoleByName(@PathVariable String nom) {
        return ResponseEntity.ok(roleService.getByNom(nom));
    }

    @Operation(summary = "Chercher un rôle par ID",
            description = "Récupère les détails d'un rôle via son identifiant unique. Réservé à l'ADMIN.")
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Integer id){
        return ResponseEntity.ok(roleService.getById(id));
    }

    //Comme demandé, seul l'admin peut supprimer un rôle
    @Operation(summary = "Supprimer un rôle",
            description = "Supprime définitivement un rôle du système. Attention aux utilisateurs liés. Réservé à l'ADMIN.")
    @SecurityRequirement(name = "BearerAuth")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}