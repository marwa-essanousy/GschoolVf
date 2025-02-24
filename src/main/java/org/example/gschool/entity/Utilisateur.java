package org.example.gschool.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {
    @Id
    @Column(name = "id_utilisateur", nullable = false)
    private Integer id;

    @Column(name = "nom_utilisateur", nullable = false)
    private String nomUtilisateur;

    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @Lob
    @Column(name = "role", nullable = false)
    private String role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}