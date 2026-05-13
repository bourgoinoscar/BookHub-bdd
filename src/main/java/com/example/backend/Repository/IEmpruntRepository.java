package com.example.backend.Repository;

import com.example.backend.Entity.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IEmpruntRepository extends JpaRepository<Emprunt, Integer> {
    // Trouver les emprunts actifs d'un utilisateur
    List<Emprunt> findByUtilisateurIdAndDateRetourEffectifIsNull(Integer userId);

    // Trouver tous les emprunts en retard (date_retour_prevu dépassée et non rendu)
    List<Emprunt> findByDateRetourPrevuBeforeAndDateRetourEffectifIsNull(LocalDate date);
}