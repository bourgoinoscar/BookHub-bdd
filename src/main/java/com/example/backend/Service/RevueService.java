package com.example.backend.Service;

import com.example.backend.Entity.Livre;
import com.example.backend.Entity.Revue;
import com.example.backend.Entity.Utilisateur;
import com.example.backend.Repository.ILivreRepository;
import com.example.backend.Repository.IRevueRepository;
import com.example.backend.Repository.IUtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RevueService {

    @Autowired
    private IRevueRepository revueRepository;

    @Autowired
    private ILivreRepository livreRepository;

    @Autowired
    private IUtilisateurRepository utilisateurRepository;

    public Revue laisserUnAvis(Integer userId, Integer livreId, Integer note, String commentaire) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));

        Revue revue = new Revue();
        revue.setUtilisateur(user);
        revue.setLivre(livre);
        revue.setNote(note);
        revue.setCommentaire(commentaire);
        revue.setDatePublication(LocalDate.now());

        return revueRepository.save(revue);
    }

    public List<Revue> findAll() {
        return revueRepository.findAll();
    }

    public List<Revue> getRevuesByLivre(Integer livreId) {
        return revueRepository.findByLivreId(livreId);
    }

    public Revue updateRevue(Integer id, Integer nouvelleNote, String nouveauCommentaire) {
        Revue revue = revueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Revue non trouvée"));

        revue.setNote(nouvelleNote);
        revue.setCommentaire(nouveauCommentaire);
        revue.setDatePublication(LocalDate.now()); // On met à jour la date

        return revueRepository.save(revue);
    }

    public void deleteRevue(Integer id) {
        revueRepository.deleteById(id);
    }
}
