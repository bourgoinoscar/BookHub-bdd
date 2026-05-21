package com.example.backend.Controller;

import com.example.backend.Dto.EmpruntDTO;
import com.example.backend.Entity.Emprunt;
import com.example.backend.Securite.UserPrincipal;
import com.example.backend.Service.EmpruntService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Emprunt", description = "Gestion des emprunts")
@RequestMapping("/api/loans")
public class EmpruntController {

    @Autowired
    private EmpruntService empruntService;

    //Quand un lecteur demande a emprunter un livre
    //Annotations pour le swagger
    @Operation(summary = "Effectuer un nouvel emprunt",
            description = "Permet à un lecteur connecté d'emprunter un livre. L'ID utilisateur est extrait du token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Emprunt créé avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès refusé : rôle LECTEUR requis"),
            @ApiResponse(responseCode = "400", description = "Erreur : Livre non disponible ou stock épuisé")
    })
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping("/loan")
    @PreAuthorize("hasAuthority('LECTEUR')")
    public ResponseEntity<EmpruntDTO> emprunterLivre(
            @RequestParam Integer livreId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        // Le service gère déjà la réduction du stock et la date de retour prévu
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(empruntService.effectuerEmprunt(userPrincipal.getId(), livreId));
    }

    //Ici, le LECTEUR voit ses emprunts passés et en cours
    @Operation(summary = "Récupérer mon historique",
            description = "Liste tous les emprunts (passés et en cours) de l'utilisateur connecté.")
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('LECTEUR')")
    public ResponseEntity<List<EmpruntDTO>> getMonHistorique(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(empruntService.getHistoriqueUtilisateur(userPrincipal.getId()));
    }

    //Quand le livre est rendu, le BIBLIOTHECAIRE valide le retour
    @Operation(summary = "Valider le retour d'un livre",
            description = "Enregistre le retour effectif d'un livre. Réservé au BIBLIOTHECAIRE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retour validé, le stock a été mis à jour"),
            @ApiResponse(responseCode = "404", description = "ID d'emprunt introuvable")
    })
    @SecurityRequirement(name = "BearerAuth")
    @PatchMapping("/return/{loanId}")
    @PreAuthorize("hasAuthority('BIBLIOTHECAIRE')")
    public ResponseEntity<EmpruntDTO> validerRetour(@PathVariable Integer loanId) {
        // Le service gère la remise en stock (+1) et la date de retour effectif
        return ResponseEntity.ok(empruntService.retournerLivre(loanId));
    }

    //Liste tous les emprunts dont la date de retour est dépassée
    @Operation(summary = "Lister les emprunts en retard",
            description = "Affiche tous les emprunts dont la date de retour prévue est dépassée. Réservé au BIBLIOTHECAIRE.")
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/late")
    @PreAuthorize("hasAuthority('BIBLIOTHECAIRE')")
    public ResponseEntity<List<EmpruntDTO>> voirLesRetards() {
        return ResponseEntity.ok(empruntService.getEmpruntsEnRetard());
    }

    //Liste tous les emprunts pour voir l'activité globale de la bibliothèque
    @Operation(summary = "Voir l'activité globale des emprunts",
            description = "Liste l'intégralité des emprunts de la bibliothèque. Réservé au BIBLIOTHECAIRE.")
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping
    @PreAuthorize("hasAuthority('BIBLIOTHECAIRE')")
    public ResponseEntity<List<EmpruntDTO>> getAllEmprunts() {
        return ResponseEntity.ok(empruntService.findAll());
    }
}