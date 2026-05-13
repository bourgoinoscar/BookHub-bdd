package com.example.backend.Controller;

import com.example.backend.Entity.Reservation;
import com.example.backend.Enum.StatutResa;
import com.example.backend.Service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    //Le lecteur peut consulter ses réservations
    @GetMapping("/my/{userId}")
    @PreAuthorize("hasRole('LECTEUR') and #userId == authentication.principal.id")
    public ResponseEntity<List<Reservation>> getMesReservations(@PathVariable Integer userId) {
        return ResponseEntity.ok(reservationService.getReservationsByUtilisateur(userId));
    }

    //Même si c'est le lecteur qui veut réserver un livre, c'est bien le bibliothecaire qui le fait
    @PostMapping
    @PreAuthorize("hasRole('BIBLIOTHECAIRE')")
    public ResponseEntity<Reservation> creerReservation(
            @RequestParam Integer userId,
            @RequestParam Integer livreId) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.creerReservation(userId, livreId));
    }

    //Comme c'est au bibliothecaire de faire la réservation, c'est aussi à lui d'annuler la resa
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('BIBLIOTHECAIRE')")
    public ResponseEntity<Void> annulerMaReservation(@PathVariable Integer id) {
        reservationService.annulerReservation(id);
        return ResponseEntity.noContent().build();
    }

    //Valider une resa
//    @PatchMapping("/{id}/valider")
//    @PreAuthorize("hasRole('BIBLIOTHECAIRE')")
//    public ResponseEntity<Reservation> validerReservation(@PathVariable Long id) {
//        return ResponseEntity.ok(reservationService.validerReservation(id));
//    }


    //Le bibliothecaire gère la file d'attente d'un ouvrage précis pour les résa
    @GetMapping("/livre/{livreId}")
    @PreAuthorize("hasRole('BIBLIOTHECAIRE')")
    public ResponseEntity<List<Reservation>> getFileAttente(@PathVariable Integer livreId, StatutResa statut) {
        return ResponseEntity.ok(reservationService.getReservationsByLivre(livreId, statut));
    }
}