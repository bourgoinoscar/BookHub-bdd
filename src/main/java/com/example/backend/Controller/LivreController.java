package com.example.backend.Controller;

import com.example.backend.Dto.LivreDTO;
import com.example.backend.Entity.Livre;
import com.example.backend.Service.LivreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@Tag(name = "Livres", description = "Gestion du catalogue de livres")
@RequestMapping("/api/books")
public class LivreController {

    @Autowired
    private LivreService livreService;

    //Accès du catalogue par tous, même non connecté
    @Operation(summary = "Lister tous les livres", description = "Récupère la liste complète des ouvrages. Accès public.")
    @GetMapping
    public ResponseEntity<List<LivreDTO>> getAllLivres() {
        return ResponseEntity.ok(livreService.getAll());
    }

    @Operation(summary = "Rechercher des livres", description = "Filtre les livres par titre, auteur, catégorie ou ISBN.")
    @GetMapping("/search")
    public ResponseEntity<List<LivreDTO>> rechercherLivre(
            @Parameter(description = "Titre partiel ou complet") @RequestParam(required = false) String titre,
            @Parameter(description = "Nom de l'auteur") @RequestParam(required = false) String auteur,
            @Parameter(description = "Nom de la catégorie") @RequestParam(required = false) String categorie,
            @Parameter(description = "Code ISBN unique") @RequestParam(required = false) String isbn) {

        if (isbn != null && !isbn.isEmpty()) {
            LivreDTO dto = livreService.getByIsbn(isbn);
            return ResponseEntity.ok(Collections.singletonList(dto));
        }

        if (titre != null && !titre.isEmpty()) {
            return ResponseEntity.ok(livreService.getByTitre(titre));
        }

        if (auteur != null && !auteur.isEmpty()) {
            return ResponseEntity.ok(livreService.getByAuteur(auteur));
        }

        if (categorie != null && !categorie.isEmpty()) {
            return ResponseEntity.ok(livreService.getByCategorie(categorie));
        }

        return ResponseEntity.ok(livreService.getAll());
    }

    @Operation(summary = "Détails d'un livre", description = "Récupère un livre spécifique par son ID.")
    @GetMapping("/{id}")
    public ResponseEntity<LivreDTO> getLivreById(@PathVariable Integer id) {
        return ResponseEntity.ok(livreService.getById(id));
    }

    @Operation(summary = "Ajouter un nouveau livre", description = "Crée un livre dans le catalogue. Réservé au BIBLIOTHECAIRE.")
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping
    @PreAuthorize("hasAuthority('BIBLIOTHECAIRE')")
    public ResponseEntity<LivreDTO> ajouterLivre(@RequestBody LivreDTO livreDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livreService.save(livreDto));
    }

    @Operation(summary = "Modifier un livre", description = "Met à jour les informations d'un livre existant. Réservé au BIBLIOTHECAIRE.")
    @SecurityRequirement(name = "BearerAuth")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('BIBLIOTHECAIRE')")
    public ResponseEntity<LivreDTO> modifierLivre(@PathVariable Integer id, @RequestBody LivreDTO livreDtoDetails) {
        return ResponseEntity.ok(livreService.update(id, livreDtoDetails));
    }

    @Operation(summary = "Supprimer un livre", description = "Supprime définitivement un livre. Réservé au BIBLIOTHECAIRE.")
    @SecurityRequirement(name = "BearerAuth")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('BIBLIOTHECAIRE')")
    public ResponseEntity<Void> supprimerLivre(@PathVariable Integer id) {
        livreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}