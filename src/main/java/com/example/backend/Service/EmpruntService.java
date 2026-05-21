package com.example.backend.Service;

import com.example.backend.Dto.EmpruntDTO;
import com.example.backend.Entity.Emprunt;
import com.example.backend.Entity.Livre;
import com.example.backend.Entity.Utilisateur;
import com.example.backend.Mapper.EmpruntMapper;
import com.example.backend.Repository.IEmpruntRepository;
import com.example.backend.Repository.ILivreRepository;
import com.example.backend.Repository.IUtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpruntService {

    @Autowired
    private IEmpruntRepository empruntRepository;

    @Autowired
    private ILivreRepository livreRepository;

    @Autowired
    private IUtilisateurRepository utilisateurRepository;


    @Transactional // Le stock n'est réduit que si l'emprunt est crée
    public EmpruntDTO effectuerEmprunt(Integer userId, Integer livreId) {
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
        emprunt.setDateRetourPrevu(LocalDate.now().plusDays(14)); // Ajout de 14 jours sur la date du jour

        // Mise à jour du stock
        livre.setQuantite(livre.getQuantite() - 1);
        livreRepository.save(livre);

        Emprunt savedEmprunt = empruntRepository.save(emprunt);
        return EmpruntMapper.toDTO(savedEmprunt);
    }

    @Transactional
    public EmpruntDTO retournerLivre(Integer empruntId) {
        Emprunt emprunt = empruntRepository.findById(empruntId)
                .orElseThrow(() -> new RuntimeException("Emprunt introuvable"));

        if (emprunt.getDateRetourEffectif() != null) {
            throw new RuntimeException("Ce livre a déjà été retourné.");
        }

        emprunt.setDateRetourEffectif(LocalDate.now());

        // On remet le livre dans le stock
        Livre livre = emprunt.getLivre();
        livre.setQuantite(livre.getQuantite() + 1);
        livreRepository.save(livre);

        Emprunt updatedEmprunt = empruntRepository.save(emprunt);
        return EmpruntMapper.toDTO(updatedEmprunt);
    }

    public List<EmpruntDTO> findAll() {
        return empruntRepository.findAll()
                .stream()
                .map(EmpruntMapper::toDTO)
                .collect(Collectors.toList());
    }

    public EmpruntDTO findById(Integer id) {
        Emprunt emprunt = empruntRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprunt avec l'ID " + id + " est introuvable"));
        return EmpruntMapper.toDTO(emprunt);
    }

    public List<EmpruntDTO> getEmpruntsEnRetard() {
        return empruntRepository.findByDateRetourPrevuBeforeAndDateRetourEffectifIsNull(LocalDate.now())
                .stream()
                .map(EmpruntMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<EmpruntDTO> getHistoriqueUtilisateur(Integer userId) {
        return empruntRepository.findByUtilisateurId(userId) //
                .stream()
                .map(EmpruntMapper::toDTO)
                .collect(Collectors.toList());
    }
}