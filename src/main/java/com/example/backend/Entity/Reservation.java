package com.example.backend.Entity;

import com.example.backend.Enum.StatutResa;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private StatutResa statut;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "id_livre")
    private Livre livre;

    public Reservation() {
    }

    public Reservation(Integer id, StatutResa statut, Utilisateur utilisateur, Livre livre) {
        this.id = id;
        this.statut = statut;
        this.utilisateur = utilisateur;
        this.livre = livre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StatutResa getStatut() {
        return statut;
    }

    public void setStatut(StatutResa statut) {
        this.statut = statut;
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
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && statut == that.statut && Objects.equals(utilisateur, that.utilisateur) && Objects.equals(livre, that.livre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, statut, utilisateur, livre);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", statut=" + statut +
                ", utilisateur=" + utilisateur +
                ", livre=" + livre +
                '}';
    }
}