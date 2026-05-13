package com.example.backend.Controller;

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
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> createRole(@RequestBody String nomRole) {
        return ResponseEntity.ok(roleService.creerRole(nomRole));
    }

    //Seul l'admin peut voir la liste des rôles
    @GetMapping
    //@PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{nom}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> getRoleByName(@PathVariable String nom) {
        return ResponseEntity.ok(roleService.getByNom(nom));
    }

    //Comme demandé, seul l'admin peut supprimer un rôle
    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        roleService.supprimerRole(id);
        return ResponseEntity.noContent().build();
    }
}