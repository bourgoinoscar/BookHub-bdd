package com.example.backend.Controller;

import com.example.backend.Entity.Revue;
import com.example.backend.Service.RevueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class RevueController {

    @Autowired
    private RevueService revueService;

    //Seul le lecteur peut mettre une note et un commentaire
    @PostMapping
    @PreAuthorize("hasRole('LECTEUR')")
    public ResponseEntity<Revue> laisserUnAvis(
            @RequestParam Integer userId,
            @RequestParam Integer livreId,
            @RequestParam Integer note,
            @RequestParam String commentaire) {

        // Sécurité : Un utilisateur ne devrait pouvoir poster qu'en son nom
        // (Vérification supplémentaire possible ici via authentication.principal)

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(revueService.laisserUnAvis(userId, livreId, note, commentaire));
    }

    //Le lecteur peut modifier sa note ou son commentaire
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LECTEUR')")
    public ResponseEntity<Revue> modifierMonAvis(
            @PathVariable Integer id,
            @RequestParam Integer note,
            @RequestParam String commentaire) {

        return ResponseEntity.ok(revueService.updateRevue(id, note, commentaire));
    }

    //Voir les avis d'un livre pour tout profils
    @GetMapping("/livre/{livreId}")
    public ResponseEntity<List<Revue>> getRevuesParLivre(@PathVariable Integer livreId) {
        return ResponseEntity.ok(revueService.getRevuesByLivre(livreId));
    }

    // --- ACCÈS MODÉRATION (BIBLIOTHÉCAIRE / ADMIN) ---

    //Supprimer un commentaire inaproprié par un bibliothécaire
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('BIBLIOTHECAIRE')")
    public ResponseEntity<Void> supprimerAvis(@PathVariable Integer id) {
        revueService.deleteRevue(id);
        return ResponseEntity.noContent().build();
    }
}