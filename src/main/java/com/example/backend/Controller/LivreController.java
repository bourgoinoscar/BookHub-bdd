package com.example.backend.Controller;

import com.example.backend.Entity.Livre;
import com.example.backend.Service.LivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class LivreController {

    @Autowired
    private LivreService livreService;

    //Accès du catalogue par tous, même non connecté
    @GetMapping
    public ResponseEntity<List<Livre>> getAllLivres() {
        return ResponseEntity.ok(livreService.getAll());
    }

    @GetMapping("/search")
    public ResponseEntity<?> rechercherLivre(   
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String auteur,
            @RequestParam(required = false) String categorie,
            @RequestParam(required = false) String isbn) {

        if (isbn != null && !isbn.isEmpty()) {
            return ResponseEntity.ok(Collections.singletonList(livreService.getByIsbn(isbn)));
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

        //Si aucun de ces paramètres n'est rempli, renvoie le catalogue entier par défaut
        return ResponseEntity.ok(livreService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livre> getLivreById(@PathVariable Integer id) {
        return ResponseEntity.ok(livreService.getById(id));
    }

    // Seule le BIBLIOTHECAIRE peut ajouter un livre
    @PostMapping
    @PreAuthorize("hasRole('BIBLIOTHECAIRE')")
    public ResponseEntity<Livre> ajouterLivre(@RequestBody Livre livre) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livreService.save(livre));
    }

    //Modifier un livre (info ou quandtité)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('BIBLIOTHECAIRE')")
    public ResponseEntity<Livre> modifierLivre(@PathVariable Integer id, @RequestBody Livre livreDetails) {
        // même logique de maj que dans le Service
        return ResponseEntity.ok(livreService.update(id, livreDetails));
    }

    //Comme demandé, seul le BIBLIOTHECAIRE peut supprimer un livre
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('BIBLIOTHECAIRE')")
    public ResponseEntity<Void> supprimerLivre(@PathVariable Integer id) {
        livreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}