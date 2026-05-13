package com.example.backend.Service;

import com.example.backend.Entity.Emprunt;
import com.example.backend.Entity.Livre;
import com.example.backend.Entity.Utilisateur;
import com.example.backend.Repository.IEmpruntRepository;
import com.example.backend.Repository.ILivreRepository;
import com.example.backend.Repository.IUtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmpruntService {

    @Autowired
    private IEmpruntRepository empruntRepository;

    @Autowired
    private ILivreRepository livreRepository;

    @Autowired
    private IUtilisateurRepository utilisateurRepository;


    @Transactional // Le stock n'est réduit que si l'emprunt est crée
    public Emprunt effectuerEmprunt(Integer userId, Integer livreId) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));

        if (livre.getQuantite() <= 0) {
            throw new RuntimeException("Plus d'exemplaires disponibles pour ce livre.");
        }

        Emprunt emprunt = new Emprunt();
        emprunt.setUtilisateur(user);
        emprunt.setLivre(livre);
        emprunt.setDateEmprunt(LocalDate.now());

        // Nous sommes parti sur 14j d'emprunt, comme marqué sur l'ennoncé
        emprunt.setDateRetourPrevu(LocalDate.now().plusDays(14));

        livre.setQuantite(livre.getQuantite() - 1);
        livreRepository.save(livre);

        return empruntRepository.save(emprunt);
    }

    @Transactional
    public Emprunt retournerLivre(Integer empruntId) {
        Emprunt emprunt = empruntRepository.findById(empruntId)
                .orElseThrow(() -> new RuntimeException("Emprunt introuvable"));

        if (emprunt.getDateRetourEffectif() != null) {
            throw new RuntimeException("Ce livre a déjà été retourné.");
        }
        // Date de retour sauvegardé
        emprunt.setDateRetourEffectif(LocalDate.now());

        //On remet le livre dans le stock
        Livre livre = emprunt.getLivre();
        livre.setQuantite(livre.getQuantite() + 1);
        livreRepository.save(livre);

        return empruntRepository.save(emprunt);
    }

    public List<Emprunt> findAll() {
        return empruntRepository.findAll();
    }

    public List<Emprunt> getEmpruntsEnRetard() {
        return empruntRepository.findByDateRetourPrevuBeforeAndDateRetourEffectifIsNull(LocalDate.now());
    }

    public List<Emprunt> getHistoriqueUtilisateur(Integer userId) {
        return empruntRepository.findByUtilisateurIdAndDateRetourEffectifIsNull(userId);
    }
}