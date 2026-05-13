package com.example.backend.Service;

import com.example.backend.Entity.Role;
import com.example.backend.Entity.Utilisateur;
import com.example.backend.Repository.IRoleRepository;
import com.example.backend.Repository.IUtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {

    @Autowired
    private IUtilisateurRepository utilisateurRepository;

    @Autowired
    private IRoleRepository roleRepository;


    public Utilisateur inscrire(Utilisateur user) {
        // 1. Vérifier si l'email est déjà utilisé
        if (utilisateurRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Erreur : Cet email est déjà utilisé.");
        }

        // 2. Assigner un rôle par défaut (ici, LECTEUR) si aucun rôle n'est spécifié
        if (user.getRole() == null) {
            Role defaultRole = roleRepository.findByNom("LECTEUR")
                    .orElseThrow(() -> new RuntimeException("Rôle par défaut non trouvé en base."));
            user.setRole(defaultRole);
        }

        return utilisateurRepository.save(user);
    }

    public Utilisateur getProfil(Integer id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + id));
    }

    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }

    public Utilisateur modifierProfil(Integer id, Utilisateur nouveauxDetails) {
        Utilisateur user = getProfil(id);

        user.setNom(nouveauxDetails.getNom());
        user.setPrenom(nouveauxDetails.getPrenom());
        user.setTel(nouveauxDetails.getTel());

        if (!user.getEmail().equals(nouveauxDetails.getEmail())) {
            if (utilisateurRepository.existsByEmail(nouveauxDetails.getEmail())) {
                throw new RuntimeException("Le nouvel email est déjà pris.");
            }
            user.setEmail(nouveauxDetails.getEmail());
        }

        return utilisateurRepository.save(user);
    }

    public Utilisateur changerRole(Integer userId, Integer roleId) {
        Utilisateur user = getProfil(userId);
        Role nouveauRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé."));

        user.setRole(nouveauRole);
        return utilisateurRepository.save(user);
    }

    public void supprimerCompte(Integer id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new RuntimeException("Impossible de supprimer : Utilisateur inexistant.");
        }
        utilisateurRepository.deleteById(id);
    }
}