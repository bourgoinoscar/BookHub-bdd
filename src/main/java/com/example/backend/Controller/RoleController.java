package com.example.backend.Controller;

import com.example.backend.Dto.RoleDTO;
import com.example.backend.Entity.Role;
import com.example.backend.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    //Comme demandé, seul l'admin peut créer de nouveaux rôles
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok(roleService.save(roleDTO));
    }

    //Seul l'admin peut voir la liste des rôles
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/searchByName/{nom}") // searchByName est la pour différencier le getRoleByName et le getRoleByID
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoleDTO> getRoleByName(@PathVariable String nom) {
        return ResponseEntity.ok(roleService.getByNom(nom));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Integer id){
        return ResponseEntity.ok(roleService.getById(id));
    }

    //Comme demandé, seul l'admin peut supprimer un rôle
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}