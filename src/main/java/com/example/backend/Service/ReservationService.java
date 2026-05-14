package com.example.backend.Service;

import com.example.backend.Dto.ReservationDTO;
import com.example.backend.Entity.Livre;
import com.example.backend.Entity.Reservation;
import com.example.backend.Entity.Utilisateur;
import com.example.backend.Enum.StatutResa;
import com.example.backend.Mapper.ReservationMapper;
import com.example.backend.Repository.ILivreRepository;
import com.example.backend.Repository.IReservationRepository;
import com.example.backend.Repository.IUtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private IReservationRepository reservationRepository;

    @Autowired
    private ILivreRepository livreRepository;

    @Autowired
    private IUtilisateurRepository utilisateurRepository;


    public ReservationDTO creerReservation(Integer userId, Integer livreId) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));

        Reservation reservation = new Reservation();
        reservation.setUtilisateur(user);
        reservation.setLivre(livre);
        reservation.setStatut(StatutResa.EN_ATTENTE);

        Reservation saved = reservationRepository.save(reservation);
        return ReservationMapper.toDTO(saved);
    }


    public ReservationDTO annulerReservation(Integer id) {
        Reservation res = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

        res.setStatut(StatutResa.ANNULEE);
        Reservation updated = reservationRepository.save(res);
        return ReservationMapper.toDTO(updated);
    }

    public ReservationDTO validerReservation(Integer id) {
        Reservation res = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

        res.setStatut(StatutResa.VALIDEE);
        Reservation updated = reservationRepository.save(res);
        return ReservationMapper.toDTO(updated);
    }

    public List<ReservationDTO> getReservationsByUtilisateur(Integer userId) {
        return reservationRepository.findByUtilisateurId(userId)
                .stream()
                .map(ReservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationDTO> getReservationsByLivre(Integer livreId) {

        return reservationRepository.findByLivreId(livreId)
                .stream()
                .map(ReservationMapper::toDTO)
                .collect(Collectors.toList());
    }
}
