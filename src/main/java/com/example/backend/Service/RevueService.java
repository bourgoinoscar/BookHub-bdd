package com.example.backend.Service;

import com.example.backend.Dto.RevueDTO;
import com.example.backend.Entity.Livre;
import com.example.backend.Entity.Revue;
import com.example.backend.Entity.Utilisateur;
import com.example.backend.Mapper.RevueMapper;
import com.example.backend.Repository.ILivreRepository;
import com.example.backend.Repository.IRevueRepository;
import com.example.backend.Repository.IUtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RevueService {

    @Autowired
    private IRevueRepository revueRepository;

    @Autowired
    private ILivreRepository livreRepository;

    @Autowired
    private IUtilisateurRepository utilisateurRepository;

    public RevueDTO laisserUnAvis(Integer userId, Integer livreId, RevueDTO revueDTO) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));

        Revue revue = new Revue();
        revue.setUtilisateur(user);
        revue.setLivre(livre);
        revue.setNote(revueDTO.note());
        revue.setCommentaire(revueDTO.commentaire());
        revue.setDatePublication(LocalDate.now());

        Revue saved = revueRepository.save(revue);
        return RevueMapper.toDTO(saved);
    }

    public List<RevueDTO> findAll() {
        return revueRepository.findAll()
                .stream()
                .map(RevueMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<RevueDTO> getRevuesByLivre(Integer livreId) {
        return revueRepository.findByLivreId(livreId)
                .stream()
                .map(RevueMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RevueDTO updateRevue(Integer id, RevueDTO revueDTO) {
        Revue revue = revueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Revue non trouvée"));

        // Mise à jour partielle : on ne change que si la valeur est fournie
        if (revueDTO.note() >= 0 && revueDTO.note() != revue.getNote() && revueDTO.note() <= 5) {
            revue.setNote(revueDTO.note());
        }
        if (revueDTO.commentaire() != null) {
            revue.setCommentaire(revueDTO.commentaire());
        }

        revue.setDatePublication(LocalDate.now()); // Date de mise à jour

        Revue updated = revueRepository.save(revue);
        return RevueMapper.toDTO(updated);
    }

    public void deleteRevue(Integer id) {
        revueRepository.deleteById(id);
    }
}
