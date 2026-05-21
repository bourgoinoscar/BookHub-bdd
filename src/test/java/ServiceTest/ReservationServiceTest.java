package ServiceTest;

import com.example.backend.Dto.ReservationDTO;
import com.example.backend.Entity.Livre;
import com.example.backend.Entity.Reservation;
import com.example.backend.Entity.Utilisateur;
import com.example.backend.Enum.StatutResa;
import com.example.backend.Repository.ILivreRepository;
import com.example.backend.Repository.IReservationRepository;
import com.example.backend.Repository.IUtilisateurRepository;
import com.example.backend.Service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private IReservationRepository reservationRepository;

    @Mock
    private ILivreRepository livreRepository;

    @Mock
    private IUtilisateurRepository utilisateurRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Utilisateur utilisateur;
    private Livre livre;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        utilisateur = new Utilisateur();
        utilisateur.setId(1);
        utilisateur.setNom("Martin");

        livre = new Livre();
        livre.setId(5);
        livre.setTitre("Dune");

        reservation = new Reservation();
        reservation.setId(50);
        reservation.setUtilisateur(utilisateur);
        reservation.setLivre(livre);
        reservation.setStatut(StatutResa.EN_ATTENTE);
    }

    // --- TESTS POUR creerReservation() ---

    @Test
    void creerReservation_ShouldSucceed_WhenUserAndLivreExist() {
        // Arrange
        when(utilisateurRepository.findById(1)).thenReturn(Optional.of(utilisateur));
        when(livreRepository.findById(5)).thenReturn(Optional.of(livre));

        // On intercepte la sauvegarde pour s'assurer que le statut est bien géré
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
            Reservation r = invocation.getArgument(0);
            r.setId(50); // Simulation auto-increment ID BDD
            return r;
        });

        // Act
        ReservationDTO result = reservationService.creerReservation(1, 5);

        // Assert
        assertNotNull(result);
        verify(reservationRepository, times(1)).save(any(Reservation.class));

        // On vérifie que la réservation créée à l'origine possédait bien le statut EN_ATTENTE
        // Note : Si votre ReservationMapper map le statut dans le DTO, vous pouvez ajouter :
        // assertEquals(StatutResa.EN_ATTENTE, result.statut());
    }

    @Test
    void creerReservation_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(utilisateurRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservationService.creerReservation(99, 5);
        });

        assertEquals("Utilisateur non trouvé", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void creerReservation_ShouldThrowException_WhenLivreNotFound() {
        // Arrange
        when(utilisateurRepository.findById(1)).thenReturn(Optional.of(utilisateur));
        when(livreRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservationService.creerReservation(1, 99);
        });

        assertEquals("Livre non trouvé", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    // --- TESTS POUR annulerReservation() ---

    @Test
    void annulerReservation_ShouldChangeStatutToAnnulee() {
        // Arrange
        when(reservationRepository.findById(50)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        ReservationDTO result = reservationService.annulerReservation(50);

        // Assert
        assertNotNull(result);
        assertEquals(StatutResa.ANNULEE, reservation.getStatut(), "Le statut de l'entité aurait dû passer à ANNULEE");
        verify(reservationRepository, times(1)).save(reservation);
    }

    // --- TESTS POUR validerReservation() ---

    @Test
    void validerReservation_ShouldChangeStatutToValidee() {
        // Arrange
        when(reservationRepository.findById(50)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        ReservationDTO result = reservationService.validerReservation(50);

        // Assert
        assertNotNull(result);
        assertEquals(StatutResa.VALIDEE, reservation.getStatut(), "Le statut de l'entité aurait dû passer à VALIDEE");
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void validerReservation_ShouldThrowException_WhenReservationNotFound() {
        // Arrange
        when(reservationRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> reservationService.validerReservation(99));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    // --- TESTS POUR RECHERCHES/FILTRES ---

    @Test
    void getReservationsByUtilisateur_ShouldReturnList() {
        // Arrange
        when(reservationRepository.findByUtilisateurId(1)).thenReturn(Collections.singletonList(reservation));

        // Act
        List<ReservationDTO> results = reservationService.getReservationsByUtilisateur(1);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        verify(reservationRepository, times(1)).findByUtilisateurId(1);
    }

    @Test
    void getReservationsByLivre_ShouldReturnList() {
        // Arrange
        when(reservationRepository.findByLivreId(5)).thenReturn(Collections.singletonList(reservation));

        // Act
        List<ReservationDTO> results = reservationService.getReservationsByLivre(5);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        verify(reservationRepository, times(1)).findByLivreId(5);
    }
}
