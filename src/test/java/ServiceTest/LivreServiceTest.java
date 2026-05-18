package ServiceTest;

import com.example.backend.Dto.LivreDTO;
import com.example.backend.Entity.Livre;
import com.example.backend.Repository.ILivreRepository;
import com.example.backend.Service.LivreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LivreServiceTest {

    @Mock
    private ILivreRepository livreRepository;

    @InjectMocks
    private LivreService livreService;

    private Livre livre;
    private LivreDTO livreDTO;

    @BeforeEach
    void setUp() {
        // Initialisation des données de test
        livre = new Livre();
        livre.setId(1);
        livre.setTitre("Le Petit Prince");
        livre.setAuteur("Antoine de Saint-Exupéry");
        livre.setCategorie("Conte");
        livre.setIsbn("123456789");
        livre.setQuantite(5);

        // Adaptation selon votre structure de DTO (ici format Record supposé)
        livreDTO = new LivreDTO(1, "Le Petit Prince", "Antoine de Saint-Exupéry", "Conte", "Résumé", "123456789", 5);
    }

    @Test
    void getById_ShouldReturnLivreDTO_WhenLivreExists() {
        // Arrange
        when(livreRepository.findById(1)).thenReturn(Optional.of(livre));

        // Act
        LivreDTO result = livreService.getById(1);

        // Assert
        assertNotNull(result);
        assertEquals("Le Petit Prince", result.titre());
        verify(livreRepository, times(1)).findById(1);
    }

    @Test
    void getById_ShouldThrowException_WhenLivreDoesNotExist() {
        // Arrange
        when(livreRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            livreService.getById(99);
        });

        assertEquals("Livre non trouvé", exception.getMessage());
    }

    @Test
    void save_ShouldReturnSavedLivreDTO_WhenIsbnIsUnique() {
        // Arrange
        when(livreRepository.findByIsbn(livreDTO.isbn())).thenReturn(Optional.empty());
        when(livreRepository.save(any(Livre.class))).thenReturn(livre);

        // Act
        LivreDTO savedDto = livreService.save(livreDTO);

        // Assert
        assertNotNull(savedDto);
        assertEquals(livreDTO.isbn(), savedDto.isbn());
        verify(livreRepository, times(1)).save(any(Livre.class));
    }

    @Test
    void save_ShouldThrowException_WhenIsbnAlreadyExists() {
        // Arrange
        when(livreRepository.findByIsbn(livreDTO.isbn())).thenReturn(Optional.of(livre));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            livreService.save(livreDTO);
        });

        assertTrue(exception.getMessage().contains("existe déjà en base"));
        verify(livreRepository, never()).save(any(Livre.class));
    }

    @Test
    void delete_ShouldDelete_WhenLivreExists() {
        // Arrange
        when(livreRepository.existsById(1)).thenReturn(true);
        doNothing().when(livreRepository).deleteById(1);

        // Act
        livreService.delete(1);

        // Assert
        verify(livreRepository, times(1)).deleteById(1);
    }
}