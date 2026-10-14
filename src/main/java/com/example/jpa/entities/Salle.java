package com.example.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Salle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(nullable = false)
    private int capacite;

    @OneToMany(mappedBy = "salle", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Utilisateur> utilisateurs = new ArrayList<>();

    // Constructeur sans argument (requis par JPA)
    public Salle() {
    }

    // Constructeur avec les informations de la salle
    public Salle(String nom, int capacite) {
        this.nom = nom;
        this.capacite = capacite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(List<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    // Méthodes d'assistance pour la relation bidirectionnelle
    public void addUtilisateur(Utilisateur utilisateur) {
        if (utilisateur != null) {
            if (this.utilisateurs == null) {
                this.utilisateurs = new ArrayList<>();
            }
            this.utilisateurs.add(utilisateur);
            utilisateur.setSalle(this);
        }
    }

    public void removeUtilisateur(Utilisateur utilisateur) {
        if (utilisateur != null && this.utilisateurs != null) {
            this.utilisateurs.remove(utilisateur);
            utilisateur.setSalle(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salle salle = (Salle) o;
        return Objects.equals(nom, salle.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }

    @Override
    public String toString() {
        return "Salle [id=" + id
                + ", nom=" + nom
                + ", capacite=" + capacite + "]";
    }
}
