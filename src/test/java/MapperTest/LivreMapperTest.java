package MapperTest;

import com.example.backend.Dto.LivreDTO;
import com.example.backend.Entity.Livre;
import com.example.backend.Mapper.LivreMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LivreMapperTest {

    @Test
    void toDTO_ShouldReturnMappedLivreDTO_WhenEntityIsNotNull() {
        // Arrange : On prépare une entité Livre avec des données fictives
        Livre livre = new Livre();
        livre.setId(42);
        livre.setTitre("Les Misérables");
        livre.setAuteur("Victor Hugo");
        livre.setCategorie("Roman");
        livre.setResume("L'histoire de Jean Valjean...");
        livre.setIsbn("9782070409228");
        livre.setQuantite(3);

        // Act : On appelle la méthode du mapper
        LivreDTO result = LivreMapper.toDTO(livre);

        // Assert : On vérifie que le DTO n'est pas nul et que tous les champs correspondent
        assertNotNull(result, "Le DTO résultant ne devrait pas être nul");
        assertEquals(42, result.id());
        assertEquals("Les Misérables", result.titre());
        assertEquals("Victor Hugo", result.auteur());
        assertEquals("Roman", result.categorie());
        assertEquals("L'histoire de Jean Valjean...", result.resume());
        assertEquals("9782070409228", result.isbn());
        assertEquals(3, result.quantite());
    }

    @Test
    void toDTO_ShouldReturnNull_WhenEntityIsNull() {
        // Act : On passe un objet null au mapper
        LivreDTO result = LivreMapper.toDTO(null);

        // Assert : On vérifie que la méthode gère correctement le null en retournant null
        assertNull(result, "Le mapper devrait retourner null si l'entité fournie est nulle");
    }
}