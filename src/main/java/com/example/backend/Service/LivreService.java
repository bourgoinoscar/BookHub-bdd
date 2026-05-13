package com.example.backend.Service;

import com.example.backend.Entity.Livre;
import com.example.backend.Repository.ILivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivreService {
    @Autowired
    private ILivreRepository livreRepository;

    public List<Livre> getAll() { return livreRepository.findAll(); }

    public Livre getById(Integer id) {
        return livreRepository.findById(id).orElseThrow(() -> new RuntimeException("Livre non trouvé"));
    }

    public List<Livre> getByTitre(String titre) {
        return livreRepository.findByTitreContainingIgnoreCase(titre);
    }

    public List<Livre> getByAuteur(String auteur) {
        return livreRepository.findByAuteurContainingIgnoreCase(auteur);
    }

    public List<Livre> getByCategorie(String categorie) {
        return livreRepository.findByCategorieContainingIgnoreCase(categorie);
    }

    public Livre getByIsbn(String isbn) {
        return livreRepository.findByIsbn(isbn).orElseThrow(() -> new RuntimeException("Livre non trouvée via l'isbn"));
    }

    public Livre update(Integer id, Livre livreDetails) {
        // 1. On vérifie si le livre existe
        Livre livreExistant = getById(id);

        // 2. On met à jour les champs (sauf l'ID)
        livreExistant.setTitre(livreDetails.getTitre());
        livreExistant.setAuteur(livreDetails.getAuteur());
        livreExistant.setCategorie(livreDetails.getCategorie());
        livreExistant.setResume(livreDetails.getResume());
        livreExistant.setIsbn(livreDetails.getIsbn());
        livreExistant.setQuantite(livreDetails.getQuantite());

        // 3. On sauvegarde les modifications
        return livreRepository.save(livreExistant);
    }

    public Livre save(Livre livre) { return livreRepository.save(livre); }

    public void delete(Integer id) { livreRepository.deleteById(id); }
}
