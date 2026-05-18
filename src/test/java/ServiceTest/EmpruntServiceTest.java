package ServiceTest;

import com.example.backend.Dto.EmpruntDTO;
import com.example.backend.Entity.Emprunt;
import com.example.backend.Entity.Livre;
import com.example.backend.Entity.Utilisateur;
import com.example.backend.Repository.IEmpruntRepository;
import com.example.backend.Repository.ILivreRepository;
import com.example.backend.Repository.IUtilisateurRepository;
import com.example.backend.Service.EmpruntService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmpruntServiceTest {

    @Mock
    private IEmpruntRepository empruntRepository;

    @Mock
    private ILivreRepository livreRepository;

    @Mock
    private IUtilisateurRepository utilisateurRepository;

    @InjectMocks
    private EmpruntService empruntService;

    private Utilisateur utilisateur;
    private Livre livre;
    private Emprunt emprunt;

    @BeforeEach
    void setUp() {
        utilisateur = new Utilisateur();
        utilisateur.setId(1);
        utilisateur.setNom("Dupont");

        livre = new Livre();
        livre.setId(10);
        livre.setTitre("1984");
        livre.setQuantite(2); // Stock initial disponible

        emprunt = new Emprunt();
        emprunt.setId(100);
        emprunt.setUtilisateur(utilisateur);
        emprunt.setLivre(livre);
        emprunt.setDateEmprunt(LocalDate.now());
        emprunt.setDateRetourPrevu(LocalDate.now().plusDays(14));
    }

    // --- TESTS POUR effectuerEmprunt() ---

    @Test
    void effectuerEmprunt_ShouldSucceed_WhenStockIsAvailable() {
        // Arrange
        when(utilisateurRepository.findById(1)).thenReturn(Optional.of(utilisateur));
        when(livreRepository.findById(10)).thenReturn(Optional.of(livre));

        // On capture l'emprunt qui va être sauvegardé pour simuler le comportement du Repository
        when(empruntRepository.save(any(Emprunt.class))).thenAnswer(invocation -> {
            Emprunt e = invocation.getArgument(0);
            e.setId(100); // On simule l'attribution de l'ID par la BDD
            return e;
        });

        // Act
        EmpruntDTO result = empruntService.effectuerEmprunt(1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, livre.getQuantite(), "Le stock du livre aurait dû diminuer de 1 (2 -> 1)");

        // Vérification des dates calculées par le service
        verify(empruntRepository).save(any(Emprunt.class));
        verify(livreRepository).save(livre);
    }

    @Test
    void effectuerEmprunt_ShouldThrowException_WhenStockIsZero() {
        // Arrange : On vide le stock du livre
        livre.setQuantite(0);
        when(utilisateurRepository.findById(1)).thenReturn(Optional.of(utilisateur));
        when(livreRepository.findById(10)).thenReturn(Optional.of(livre));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            empruntService.effectuerEmprunt(1, 10);
        });

        assertEquals("Plus d'exemplaires disponibles pour ce livre.", exception.getMessage());
        assertEquals(0, livre.getQuantite(), "Le stock ne doit pas passer en négatif");

        // On s'assure qu'aucun enregistrement n'a été tenté
        verify(empruntRepository, never()).save(any(Emprunt.class));
    }

    @Test
    void effectuerEmprunt_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(utilisateurRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> empruntService.effectuerEmprunt(99, 10));
    }

    // --- TESTS POUR retournerLivre() ---

    @Test
    void retournerLivre_ShouldSucceed_WhenBookNotYetReturned() {
        // Arrange
        emprunt.setDateRetourEffectif(null); // Sûr qu'il n'est pas encore rendu
        when(empruntRepository.findById(100)).thenReturn(Optional.of(emprunt));
        when(empruntRepository.save(any(Emprunt.class))).thenReturn(emprunt);

        // Act
        EmpruntDTO result = empruntService.retournerLivre(100);

        // Assert
        assertNotNull(emprunt.getDateRetourEffectif(), "La date de retour effectif doit être renseignée à aujourd'hui");
        assertEquals(LocalDate.now(), emprunt.getDateRetourEffectif());
        assertEquals(3, livre.getQuantite(), "Le stock de livre aurait dû augmenter de 1 (2 -> 3)");

        verify(livreRepository, times(1)).save(livre);
        verify(empruntRepository, times(1)).save(emprunt);
    }

    @Test
    void retournerLivre_ShouldThrowException_WhenBookIsAlreadyReturned() {
        // Arrange : Le livre est déjà retourné
        emprunt.setDateRetourEffectif(LocalDate.now().minusDays(1));
        when(empruntRepository.findById(100)).thenReturn(Optional.of(emprunt));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            empruntService.retournerLivre(100);
        });

        assertEquals("Ce livre a déjà été retourné.", exception.getMessage());
        assertEquals(2, livre.getQuantite(), "Le stock ne doit pas bouger si l'action échoue");

        verify(empruntRepository, never()).save(any(Emprunt.class));
        verify(livreRepository, never()).save(any(Livre.class));
    }
}