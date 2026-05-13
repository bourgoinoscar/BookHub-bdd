package com.example.backend.Repository;

import com.example.backend.Entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    // Pour la connexion
    Optional<Utilisateur> findByEmail(String email);

    // Pour l'inscription
    boolean existsByEmail(String email);
}