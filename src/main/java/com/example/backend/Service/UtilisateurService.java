package com.example.backend.Service;

import com.example.backend.Dto.UtilisateurDTO;
import com.example.backend.Entity.Role;
import com.example.backend.Entity.Utilisateur;
import com.example.backend.Mapper.UtilisateurMapper;
import com.example.backend.Repository.IRoleRepository;
import com.example.backend.Repository.IUtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    @Autowired
    private IUtilisateurRepository utilisateurRepository;

    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public UtilisateurDTO save(UtilisateurDTO dto) {
        // 1. Vérifier si l'email est déjà utilisé
        if (utilisateurRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Erreur : Cet email est déjà utilisé.");
        }

        // 2. Conversion DTO -> Entité
        Utilisateur user = new Utilisateur();
        user.setNom(dto.nom());
        user.setPrenom(dto.prenom());
        user.setEmail(dto.email());
        user.setTel(dto.tel());
        user.setDateNaissance(dto.dateDeNaissance());
        // Encodage du mot de passe pour Spring Security
        user.setPassword(passwordEncoder.encode(dto.password()));

        // 3. Gestion du rôle
        Role role;
        if (dto.roleId() != null) {
            // On cherche le rôle spécifique fourni par l'ID
            role = roleRepository.findById(dto.roleId())
                    .orElseThrow(() -> new RuntimeException("Rôle avec l'ID " + dto.roleId() + " non trouvé."));
        } else {
            // Rôle par défaut si aucun ID n'est envoyé
            role = roleRepository.findByNom("LECTEUR")
                    .orElseThrow(() -> new RuntimeException("Rôle 'LECTEUR' non trouvé en base."));
        }
        user.setRole(role);

        Utilisateur saved = utilisateurRepository.save(user);
        return UtilisateurMapper.toDTO(saved);
    }

    public UtilisateurDTO findById(Integer id) {
        Utilisateur user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + id));
        return UtilisateurMapper.toDTO(user);
    }

    public List<UtilisateurDTO> findAll() {
        return utilisateurRepository.findAll()
                .stream()
                .map(UtilisateurMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UtilisateurDTO modifierProfil(Integer id, UtilisateurDTO utilisateurDTO) {
        // On récupère l'entité existante
        Utilisateur user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Mise à jour partielle (on ne change que si ce n'est pas null dans le DTO)
        if (utilisateurDTO.nom() != null) user.setNom(utilisateurDTO.nom());
        if (utilisateurDTO.prenom() != null) user.setPrenom(utilisateurDTO.prenom());
        if (utilisateurDTO.dateDeNaissance() != null) user.setDateNaissance(utilisateurDTO.dateDeNaissance());


        if (utilisateurDTO.tel() != null) user.setTel(utilisateurDTO.tel());

        if(utilisateurDTO.password() !=null ) user.setPassword(passwordEncoder.encode(utilisateurDTO.password()));

        //Gestion spécifique de l'email
        if (utilisateurDTO.email() != null && !user.getEmail().equals(utilisateurDTO.email())) {
            if (utilisateurRepository.existsByEmail(utilisateurDTO.email())) {
                throw new RuntimeException("Le nouvel email est déjà pris.");
            }
            user.setEmail(utilisateurDTO.email());
        }

        Utilisateur updated = utilisateurRepository.save(user);
        return UtilisateurMapper.toDTO(updated);
    }

    public UtilisateurDTO changerRole(Integer userId, Integer roleId) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé."));

        Role nouveauRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé."));

        user.setRole(nouveauRole);
        Utilisateur updated = utilisateurRepository.save(user);
        return UtilisateurMapper.toDTO(updated);
    }

    public void delete(Integer id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new RuntimeException("Impossible de supprimer : Utilisateur inexistant.");
        }
        utilisateurRepository.deleteById(id);
    }
}