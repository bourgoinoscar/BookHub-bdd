package com.example.backend.Controller;

import com.example.backend.Dto.EmpruntDTO;
import com.example.backend.Entity.Emprunt;
import com.example.backend.Service.EmpruntService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class EmpruntController {

    @Autowired
    private EmpruntService empruntService;

    //Quand un lecteur demande a emprunter un livre
    @PostMapping("/loan")
    //@PreAuthorize("hasRole('LECTEUR')")
    public ResponseEntity<EmpruntDTO> emprunterLivre(
            @RequestParam Integer userId,
            @RequestParam Integer livreId) {
        // Le service gère déjà la réduction du stock et la date de retour prévu
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(empruntService.effectuerEmprunt(userId, livreId));
    }

    //Ici, le LECTEUR voit ses emprunts passés et en cours
    @GetMapping("/my/{userId}")
    //@PreAuthorize("hasRole('LECTEUR') and #userId == authentication.principal.id")
    public ResponseEntity<List<EmpruntDTO>> getMonHistorique(@PathVariable Integer userId) {
        return ResponseEntity.ok(empruntService.getHistoriqueUtilisateur(userId));
    }

    //Quand le livre est rendu, le BIBLIOTHECAIRE valide le retour
    @PatchMapping("/return/{loanId}")
    //@PreAuthorize("hasRole('BIBLIOTHECAIRE')")
    public ResponseEntity<EmpruntDTO> validerRetour(@PathVariable Integer loanId) {
        // Le service gère la remise en stock (+1) et la date de retour effectif
        return ResponseEntity.ok(empruntService.retournerLivre(loanId));
    }

    //Liste tous les emprunts dont la date de retour est dépassée
    @GetMapping("/late")
    //@PreAuthorize("hasRole('BIBLIOTHECAIRE')")
    public ResponseEntity<List<EmpruntDTO>> voirLesRetards() {
        return ResponseEntity.ok(empruntService.getEmpruntsEnRetard());
    }

    //Liste tous les emprunts pour voir l'activité globale de la bibliothèque
    @GetMapping
    //@PreAuthorize("hasRole('BIBLIOTHECAIRE')")
    public ResponseEntity<List<EmpruntDTO>> getAllEmprunts() {
        return ResponseEntity.ok(empruntService.findAll());
    }
}