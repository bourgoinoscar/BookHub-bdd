package com.example.backend.Repository;

import com.example.backend.Entity.Reservation;
import com.example.backend.Enum.StatutResa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByUtilisateurId(Integer utilisateurId);
    List<Reservation> findByLivreId(Integer livreId);
}
