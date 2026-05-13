package com.example.backend.Controller;

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
    public ResponseEntity<Utilisateur> inscription(@RequestBody Utilisateur utilisateur) {
        // Comme mis dans le Service, le role par défaut sera LECTEUR
        return ResponseEntity.ok(utilisateurService.inscrire(utilisateur));
    }

    //L'admin peut voir tous les LECTEUR et BIBLIOTHECAIRE
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        return ResponseEntity.ok(utilisateurService.findAll());
    }

    //Ici, l'amdin peut voir le profile de n'importe qui, l'utilisateur propriétaire peut acceder à son profile
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Utilisateur> getProfil(@PathVariable Integer id) {
        return ResponseEntity.ok(utilisateurService.getProfil(id));
    }

    //L'admin pour l'utilisateur propriétaire peuvent modifier le profile
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Utilisateur> modifierProfil(
            @PathVariable Integer id,
            @RequestBody Utilisateur nouveauxDetails) {

        Utilisateur utilisateurMisAJour = utilisateurService.modifierProfil(id, nouveauxDetails);
        return ResponseEntity.ok(utilisateurMisAJour);
    }

    // Seul l'admin peut modifier un role, le passer de LECTEUR à BIBLIOTHECAIRE
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Utilisateur> changerRole(@PathVariable Integer id, @RequestParam Integer roleId) {
        return ResponseEntity.ok(utilisateurService.changerRole(id, roleId));
    }

    //Pour supprimer un Utilisateur, seul l'admin le peut
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> supprimerUtilisateur(@PathVariable Integer id) {
        utilisateurService.supprimerCompte(id);
        return ResponseEntity.noContent().build();
    }
}