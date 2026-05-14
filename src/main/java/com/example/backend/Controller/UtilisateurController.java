package com.example.backend.Controller;

import com.example.backend.Dto.UtilisateurDTO;
import com.example.backend.Entity.Utilisateur;
import com.example.backend.Service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/register")
    public ResponseEntity<UtilisateurDTO> inscription(@RequestBody UtilisateurDTO utilisateur) {
        // Comme mis dans le Service, le role par défaut sera LECTEUR
        return ResponseEntity.ok(utilisateurService.save(utilisateur));
    }

    //L'admin peut voir tous les LECTEUR et BIBLIOTHECAIRE
    @GetMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UtilisateurDTO>> getAllUtilisateurs() {
        return ResponseEntity.ok(utilisateurService.findAll());
    }

    //Ici, l'amdin peut voir le profile de n'importe qui, l'utilisateur propriétaire peut acceder à son profile
    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UtilisateurDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(utilisateurService.findById(id));
    }

    //L'admin pour l'utilisateur propriétaire peuvent modifier le profile
    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UtilisateurDTO> modifierProfil(
            @PathVariable Integer id,
            @RequestBody UtilisateurDTO utilisateurDTO) {

        UtilisateurDTO utilisateurMisAJour = utilisateurService.modifierProfil(id, utilisateurDTO);
        return ResponseEntity.ok(utilisateurMisAJour);
    }

    // Seul l'admin peut modifier un role, le passer de LECTEUR à BIBLIOTHECAIRE
    @PatchMapping("/{id}/role")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UtilisateurDTO> changerRole(@PathVariable Integer id, @RequestParam Integer roleId) {
        return ResponseEntity.ok(utilisateurService.changerRole(id, roleId));
    }

    //Pour supprimer un Utilisateur, seul l'admin le peut
    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> supprimerUtilisateur(@PathVariable Integer id) {
        utilisateurService.delete(id);
        return ResponseEntity.noContent().build();
    }
}