package com.example.backend.Controller;

import com.example.backend.Dto.LivreDTO;
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
    public ResponseEntity<List<LivreDTO>> getAllLivres() {
        return ResponseEntity.ok(livreService.getAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<LivreDTO>> rechercherLivre(
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String auteur,
            @RequestParam(required = false) String categorie,
            @RequestParam(required = false) String isbn) {

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

    @GetMapping("/{id}")
    public ResponseEntity<LivreDTO> getLivreById(@PathVariable Integer id) {
        return ResponseEntity.ok(livreService.getById(id));
    }


    @PostMapping
    @PreAuthorize("hasAuthority('BIBLIOTHECAIRE')")
    public ResponseEntity<LivreDTO> ajouterLivre(@RequestBody LivreDTO livreDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livreService.save(livreDto));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('BIBLIOTHECAIRE')")
    public ResponseEntity<LivreDTO> modifierLivre(@PathVariable Integer id, @RequestBody LivreDTO livreDtoDetails) {
        return ResponseEntity.ok(livreService.update(id, livreDtoDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('BIBLIOTHECAIRE')")
    public ResponseEntity<Void> supprimerLivre(@PathVariable Integer id) {
        livreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}