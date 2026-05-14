package com.example.backend.Controller;

import com.example.backend.Dto.RevueDTO;
import com.example.backend.Entity.Revue;
import com.example.backend.Securite.UserPrincipal;
import com.example.backend.Service.RevueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class RevueController {

    @Autowired
    private RevueService revueService;

    //Seul le lecteur peut mettre une note et un commentaire
    @PostMapping
    @PreAuthorize("hasAuthority('LECTEUR')")
    public ResponseEntity<RevueDTO> laisserUnAvis(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam Integer livreId,
            @RequestBody RevueDTO revue) {


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(revueService.laisserUnAvis(userPrincipal.getId(), livreId, revue));
    }

    //Le lecteur peut modifier sa note ou son commentaire
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('LECTEUR')")
    public ResponseEntity<RevueDTO> modifierMonAvis(
            @PathVariable Integer id,
            @RequestBody RevueDTO revue) {

        return ResponseEntity.ok(revueService.updateRevue(id,revue));
    }

    @GetMapping
    public ResponseEntity<List<RevueDTO>> findAll(){
        return ResponseEntity.ok(revueService.findAll());
    }

    //Voir les avis d'un livre pour tout profils
    @GetMapping("/book/{livreId}")
    public ResponseEntity<List<RevueDTO>> getRevuesParLivre(@PathVariable Integer livreId) {
        return ResponseEntity.ok(revueService.getRevuesByLivre(livreId));
    }

    //Supprimer un commentaire inaproprié par un bibliothécaire ou admin
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('BIBLIOTHECAIRE','ADMIN')")
    public ResponseEntity<Void> supprimerAvis(@PathVariable Integer id) {
        revueService.deleteRevue(id);
        return ResponseEntity.noContent().build();
    }
}