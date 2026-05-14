package com.example.backend.Service;

import com.example.backend.Dto.RoleDTO;
import com.example.backend.Entity.Livre;
import com.example.backend.Entity.Role;
import com.example.backend.Mapper.LivreMapper;
import com.example.backend.Mapper.RoleMapper;
import com.example.backend.Repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private IRoleRepository roleRepository;

    public RoleDTO save(RoleDTO roleDTO) {
        String nomClean = roleDTO.nom().toUpperCase().trim();

        if (roleRepository.existsByNom(nomClean)) {
            throw new RuntimeException("Le rôle " + nomClean + " existe déjà.");
        }

        Role role = new Role();
        role.setNom(nomClean);

        Role saved = roleRepository.save(role);
        return RoleMapper.toDTO(saved);
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream().map(RoleMapper::toDTO).collect(Collectors.toList());
    }

    public RoleDTO getByNom(String nom) {
        Role role = roleRepository.findByNom(nom.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Rôle '" + nom + "' introuvable."));
        return RoleMapper.toDTO(role);
    }

    public RoleDTO getById(Integer id){
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
        return RoleMapper.toDTO(role);
    }

    public void delete(Integer id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Rôle introuvable.");
        }
        roleRepository.deleteById(id);
    }
}
