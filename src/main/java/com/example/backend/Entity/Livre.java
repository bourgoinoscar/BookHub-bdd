package com.example.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
public class Livre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String titre;
    @NotNull
    private String auteur;
    @NotNull
    private String categorie;
    @NotNull
    private String resume;
    @NotNull
    private String isbn;
    @NotNull
    private int quantite;
    @NotNull
    private LocalDate dateAjout;

    @OneToMany(mappedBy = "livre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Emprunt> emprunts;

    @OneToMany(mappedBy = "livre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "livre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Revue> revues;

    @PrePersist
    protected void onCreate() {
        this.dateAjout = LocalDate.now();
    }

    public Livre() {
    }

    public Livre(Integer id, String titre, String auteur, String categorie, String resume, String isbn, int quantite, LocalDate dateAjout, List<Emprunt> emprunts) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.categorie = categorie;
        this.resume = resume;
        this.isbn = isbn;
        this.quantite = quantite;
        this.dateAjout = dateAjout;
        this.emprunts = emprunts;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public LocalDate getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(LocalDate dateAjout) {
        this.dateAjout = dateAjout;
    }

    public List<Emprunt> getEmprunts() {
        return emprunts;
    }

    public void setEmprunts(List<Emprunt> emprunts) {
        this.emprunts = emprunts;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Livre livre = (Livre) o;
        return quantite == livre.quantite && Objects.equals(id, livre.id) && Objects.equals(titre, livre.titre) && Objects.equals(auteur, livre.auteur) && Objects.equals(categorie, livre.categorie) && Objects.equals(resume, livre.resume) && Objects.equals(isbn, livre.isbn) && Objects.equals(dateAjout, livre.dateAjout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titre, auteur, categorie, resume, isbn, quantite, dateAjout);
    }

    @Override
    public String toString() {
        return "Livre{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", auteur='" + auteur + '\'' +
                ", categorie='" + categorie + '\'' +
                ", resume='" + resume + '\'' +
                ", isbn='" + isbn + '\'' +
                ", quantite=" + quantite +
                ", dateAjout=" + dateAjout +
                '}';
    }
}
