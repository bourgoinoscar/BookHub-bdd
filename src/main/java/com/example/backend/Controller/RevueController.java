package com.example.backend.Controller;

import com.example.backend.Dto.RevueDTO;
import com.example.backend.Entity.Revue;
import com.example.backend.Securite.UserPrincipal;
import com.example.backend.Service.RevueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Revues", description = "Gestion des revues sur les livres")
@RequestMapping("/api/review")
public class RevueController {

    @Autowired
    private RevueService revueService;

    //Seul le lecteur peut mettre une note et un commentaire
    @Operation(summary = "Laisser un avis",
            description = "Permet à un lecteur de publier une note et un commentaire sur un livre. L'ID du lecteur est récupéré via le token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avis publié avec succès"),
            @ApiResponse(responseCode = "403", description = "Seuls les lecteurs peuvent laisser un avis")
    })
    @SecurityRequirement(name = "BearerAuth")
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
    @Operation(summary = "Modifier mon avis",
            description = "Permet à un lecteur de modifier son commentaire ou sa note existante.")
    @SecurityRequirement(name = "BearerAuth")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('LECTEUR')")
    public ResponseEntity<RevueDTO> modifierMonAvis(
            @Parameter(description = "ID de la revue (avis)")  @PathVariable Integer id,
            @RequestBody RevueDTO revue) {

        return ResponseEntity.ok(revueService.updateRevue(id,revue));
    }

    @Operation(summary = "Lister tous les avis", description = "Récupère l'intégralité des avis de la plateforme. Accès public.")
    @GetMapping
    public ResponseEntity<List<RevueDTO>> findAll(){
        return ResponseEntity.ok(revueService.findAll());
    }

    //Voir les avis d'un livre pour tout profils
    @Operation(summary = "Voir les avis d'un livre", description = "Récupère tous les commentaires et notes pour un ouvrage spécifique. Accès public.")
    @GetMapping("/book/{livreId}")
    public ResponseEntity<List<RevueDTO>> getRevuesParLivre(@Parameter(description = "ID du livre concerné") @PathVariable Integer livreId) {
        return ResponseEntity.ok(revueService.getRevuesByLivre(livreId));
    }

    //Supprimer un commentaire inaproprié par un bibliothécaire ou admin
    @Operation(summary = "Supprimer un avis",
            description = "Permet de supprimer un avis (modération). Réservé au BIBLIOTHECAIRE ou à l'ADMIN.")
    @SecurityRequirement(name = "BearerAuth")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('BIBLIOTHECAIRE','ADMIN')")
    public ResponseEntity<Void> supprimerAvis(@Parameter(description = "ID de la revue à supprimer") @PathVariable Integer id) {
        revueService.deleteRevue(id);
        return ResponseEntity.noContent().build();
    }
}