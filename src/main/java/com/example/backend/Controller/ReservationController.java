package com.example.backend.Controller;

import com.example.backend.Dto.ReservationDTO;
import com.example.backend.Entity.Reservation;
import com.example.backend.Enum.StatutResa;
import com.example.backend.Securite.UserPrincipal;
import com.example.backend.Service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "Reservations", description = "Gestion des reservations")
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    //Le lecteur peut consulter ses réservations
    @Operation(summary = "Consulter mes réservations",
            description = "Permet au lecteur connecté de voir l'état de ses propres demandes. L'ID est extrait du token.")
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('LECTEUR')")
    public ResponseEntity<List<ReservationDTO>> getMesReservations(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(reservationService.getReservationsByUtilisateur(userPrincipal.getId()));
    }

    //Même si c'est le lecteur qui veut réserver un livre, c'est bien le bibliothecaire qui le fait
    @Operation(summary = "Créer une réservation",
            description = "Enregistre une demande de réservation. Action réservée au BIBLIOTHECAIRE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Réservation enregistrée"),
            @ApiResponse(responseCode = "403", description = "Droits insuffisants")
    })
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping
    @PreAuthorize("hasAuthority('BIBLIOTHECAIRE')")
    public ResponseEntity<ReservationDTO> creerReservation(
            @Parameter(description = "ID de l'utilisateur concerné") @RequestParam Integer userId,
            @Parameter(description = "ID du livre à réserver") @RequestParam Integer livreId) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.creerReservation(userId, livreId));
    }

    //Comme c'est au bibliothecaire de faire la réservation, c'est aussi à lui d'annuler la resa
    @Operation(summary = "Annuler une réservation",
            description = "Supprime une réservation de la liste. Action réservée au BIBLIOTHECAIRE.")
    @SecurityRequirement(name = "BearerAuth")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('BIBLIOTHECAIRE')")
    public ResponseEntity<ReservationDTO> annulerMaReservation(@Parameter(description = "ID unique de la réservation") @PathVariable Integer id) {
        return ResponseEntity.ok(reservationService.annulerReservation(id));
    }

    @Operation(summary = "Valider une réservation",
            description = "Passe le statut de la réservation à 'Validée' (ex: le livre est arrivé). Action réservée au BIBLIOTHECAIRE.")
    @SecurityRequirement(name = "BearerAuth")
    @PatchMapping("/{id}/valider")
    @PreAuthorize("hasAuthority('BIBLIOTHECAIRE')")
    public ResponseEntity<ReservationDTO> validerReservation(@Parameter(description = "ID de la réservation à valider") @PathVariable Integer id) {
        return ResponseEntity.ok(reservationService.validerReservation(id));
    }


    //Le bibliothecaire gère la file d'attente d'un ouvrage précis pour les résa
    @Operation(summary = "Voir la file d'attente d'un livre",
            description = "Affiche toutes les réservations en cours pour un ouvrage spécifique. Réservé au BIBLIOTHECAIRE.")
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/livre/{livreId}")
    @PreAuthorize("hasAuthority('BIBLIOTHECAIRE')")
    public ResponseEntity<List<ReservationDTO>> getFileAttente(@Parameter(description = "ID du livre concerné") @PathVariable Integer livreId) {
        return ResponseEntity.ok(reservationService.getReservationsByLivre(livreId));
    }
}