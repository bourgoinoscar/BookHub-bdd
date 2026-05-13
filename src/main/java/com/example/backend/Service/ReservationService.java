package com.example.backend.Service;

import com.example.backend.Entity.Livre;
import com.example.backend.Entity.Reservation;
import com.example.backend.Entity.Utilisateur;
import com.example.backend.Enum.StatutResa;
import com.example.backend.Repository.ILivreRepository;
import com.example.backend.Repository.IReservationRepository;
import com.example.backend.Repository.IUtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private IReservationRepository reservationRepository;

    @Autowired
    private ILivreRepository livreRepository;

    @Autowired
    private IUtilisateurRepository utilisateurRepository;


    public Reservation creerReservation(Integer userId, Integer livreId) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));

        Reservation reservation = new Reservation();
        reservation.setUtilisateur(user);
        reservation.setLivre(livre);
        reservation.setStatut(StatutResa.EN_ATTENTE); // Statut initial par défaut

        return reservationRepository.save(reservation);
    }


    public void annulerReservation(Integer id) {
        Reservation res = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

        res.setStatut(StatutResa.ANNULEE);
        reservationRepository.save(res);
    }

    public Reservation validerReservation(Integer id) {
        Reservation res = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

        res.setStatut(StatutResa.VALIDEE);
        return reservationRepository.save(res);
    }

    public List<Reservation> getReservationsByUtilisateur(Integer userId) {
        return reservationRepository.findByUtilisateurId(userId);
    }

    public List<Reservation> getReservationsByLivre(Integer livreId, StatutResa statut) {
        return reservationRepository.findByLivreIdAndStatut(livreId, statut);
    }
}
