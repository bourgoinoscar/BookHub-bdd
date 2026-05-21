package com.example.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Emprunt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private LocalDate dateEmprunt;
    @NotNull
    private LocalDate dateRetourPrevu;

    private LocalDate dateRetourEffectif;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "id_livre")
    private Livre livre;

    public Emprunt() {
    }

    public Emprunt(Integer id, LocalDate dateEmprunt, LocalDate dateRetourPrevu, LocalDate dateRetourEffectif, Utilisateur utilisateur, Livre livre) {
        this.id = id;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevu = dateRetourPrevu;
        this.dateRetourEffectif = dateRetourEffectif;
        this.utilisateur = utilisateur;
        this.livre = livre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public LocalDate getDateRetourPrevu() {
        return dateRetourPrevu;
    }

    public void setDateRetourPrevu(LocalDate dateRetourPrevu) {
        this.dateRetourPrevu = dateRetourPrevu;
    }

    public LocalDate getDateRetourEffectif() {
        return dateRetourEffectif;
    }

    public void setDateRetourEffectif(LocalDate dateRetourEffectif) {
        this.dateRetourEffectif = dateRetourEffectif;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Emprunt emprunt = (Emprunt) o;
        return Objects.equals(id, emprunt.id) && Objects.equals(dateEmprunt, emprunt.dateEmprunt) && Objects.equals(dateRetourPrevu, emprunt.dateRetourPrevu) && Objects.equals(dateRetourEffectif, emprunt.dateRetourEffectif) && Objects.equals(utilisateur, emprunt.utilisateur) && Objects.equals(livre, emprunt.livre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateEmprunt, dateRetourPrevu, dateRetourEffectif, utilisateur, livre);
    }

    @Override
    public String toString() {
        return "Emprunt{" +
                "id=" + id +
                ", dateEmprunt=" + dateEmprunt +
                ", dateRetourPrevu=" + dateRetourPrevu +
                ", dateRetourEffectif=" + dateRetourEffectif +
                ", utilisateur=" + utilisateur +
                ", livre=" + livre +
                '}';
    }
}
