package com.example.backend.Repository;

import com.example.backend.Entity.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ILivreRepository extends JpaRepository<Livre, Integer> {

    List<Livre> findByTitreContainingIgnoreCase(String titre);
    List<Livre> findByAuteurContainingIgnoreCase(String auteur);
    List<Livre> findByCategorieContainingIgnoreCase(String categorie);
    Optional<Livre> findByIsbn(String isbn);
}