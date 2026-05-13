package com.example.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Revue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private int note;
    @NotNull
    private String commentaire;
    @NotNull
    private LocalDate datePublication;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "id_livre")
    private Livre livre;

    public Revue() {
    }

    public Revue(Integer id, int note, String commentaire, LocalDate datePublication, Utilisateur utilisateur, Livre livre) {
        this.id = id;
        this.note = note;
        this.commentaire = commentaire;
        this.datePublication = datePublication;
        this.utilisateur = utilisateur;
        this.livre = livre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public LocalDate getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDate datePublication) {
        this.datePublication = datePublication;
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
        Revue revue = (Revue) o;
        return note == revue.note && Objects.equals(id, revue.id) && Objects.equals(commentaire, revue.commentaire) && Objects.equals(datePublication, revue.datePublication) && Objects.equals(utilisateur, revue.utilisateur) && Objects.equals(livre, revue.livre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, note, commentaire, datePublication, utilisateur, livre);
    }

    @Override
    public String toString() {
        return "Revue{" +
                "id=" + id +
                ", note=" + note +
                ", commentaire='" + commentaire + '\'' +
                ", datePublication=" + datePublication +
                ", utilisateur=" + utilisateur +
                ", livre=" + livre +
                '}';
    }
}