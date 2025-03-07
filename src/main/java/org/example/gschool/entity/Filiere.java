package org.example.gschool.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "filiere")
public class Filiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-génère l'ID
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "description")
    private String description;

    @Size(max = 255)
    @NotNull(message = "Le nom de la filière est obligatoire")
    @Column(name = "nom", nullable = false)
    private String nom;

    @Transient
    private Integer nombre_etudiant;

    public Integer getNombre_etudiant() {
        return nombre_etudiant;
    }

    public void setNombre_etudiant(Integer nombre_etudiant) {
        this.nombre_etudiant = nombre_etudiant;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
