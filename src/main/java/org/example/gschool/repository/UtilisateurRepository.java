package org.example.gschool.repository;

import org.example.gschool.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Optional<Utilisateur> findByEmailAndMotDePasse(String email, String motDePasse);
    Optional<Utilisateur> findByEmail(String email);
}
