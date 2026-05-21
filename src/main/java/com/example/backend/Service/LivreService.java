package com.example.backend.Service;

import com.example.backend.Dto.LivreDTO;
import com.example.backend.Entity.Livre;
import com.example.backend.Mapper.LivreMapper;
import com.example.backend.Repository.ILivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LivreService {
    @Autowired
    private ILivreRepository livreRepository;

    public List<LivreDTO> getAll() {
        return livreRepository.findAll().stream()
                .map(LivreMapper::toDTO)
                .collect(Collectors.toList());
    }

    public LivreDTO getById(Integer id) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));
        return LivreMapper.toDTO(livre);
    }

    public List<LivreDTO> getByTitre(String titre) {
        return livreRepository.findByTitreContainingIgnoreCase(titre)
                .stream()
                .map(LivreMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<LivreDTO> getByAuteur(String auteur) {
        return livreRepository.findByAuteurContainingIgnoreCase(auteur)
                .stream()
                .map(LivreMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<LivreDTO> getByCategorie(String categorie) {
        return livreRepository.findByCategorieContainingIgnoreCase(categorie)
                .stream()
                .map(LivreMapper::toDTO)
                .collect(Collectors.toList());
    }

    public LivreDTO getByIsbn(String isbn) {
        Livre livre = livreRepository.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé via l'isbn"));
        return LivreMapper.toDTO(livre);
    }

    public LivreDTO update(Integer id, LivreDTO livreDetailsDto) {
        Livre livreExistant = livreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé pour mise à jour"));


        if (livreDetailsDto.titre() != null) livreExistant.setTitre(livreDetailsDto.titre());
        if (livreDetailsDto.auteur() != null) livreExistant.setAuteur(livreDetailsDto.auteur());
        if (livreDetailsDto.categorie() != null) livreExistant.setCategorie(livreDetailsDto.categorie());
        if (livreDetailsDto.resume() != null) livreExistant.setResume(livreDetailsDto.resume());
        if (livreDetailsDto.isbn() != null) livreExistant.setIsbn(livreDetailsDto.isbn());

        if (livreDetailsDto.quantite() >= 0) {
            livreExistant.setQuantite(livreDetailsDto.quantite());
        }

        Livre updated = livreRepository.save(livreExistant);
        return LivreMapper.toDTO(updated);
    }

    public LivreDTO save(LivreDTO livreDto) {
        //On vérifie que le livre n'existe déjà pas en BBD
        Optional<Livre> l = livreRepository.findByIsbn(livreDto.isbn());
        if (l.isPresent()){
            throw new RuntimeException("Erreur : Un livre avec l'ISBN " + livreDto.isbn() + " existe déjà en base.");
        }


        Livre livre = new Livre();
        livre.setTitre(livreDto.titre());
        livre.setAuteur(livreDto.auteur());
        livre.setCategorie(livreDto.categorie());
        livre.setResume(livreDto.resume());
        livre.setIsbn(livreDto.isbn());
        livre.setQuantite(livreDto.quantite());

        Livre saved = livreRepository.save(livre);
        return LivreMapper.toDTO(saved); }

    public void delete(Integer id) {
        if (!livreRepository.existsById(id)) {
        throw new RuntimeException("Impossible de supprimer : livre inexistant");
    }
        livreRepository.deleteById(id);
    }
}
