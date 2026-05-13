package com.example.backend.Service;

import com.example.backend.Entity.Role;
import com.example.backend.Repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private IRoleRepository roleRepository;

    public Role creerRole(String nomRole) {
        String nomClean = nomRole.toUpperCase().trim();

        if (roleRepository.existsByNom(nomClean)) {
            throw new RuntimeException("Le rôle " + nomClean + " existe déjà.");
        }

        Role role = new Role();
        role.setNom(nomClean);
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getByNom(String nom) {
        return roleRepository.findByNom(nom.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Rôle '" + nom + "' introuvable."));
    }

    public void supprimerRole(Integer id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Rôle introuvable.");
        }
        roleRepository.deleteById(id);
    }
}
