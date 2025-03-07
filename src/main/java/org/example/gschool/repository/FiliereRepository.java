package org.example.gschool.repository;

import org.example.gschool.entity.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FiliereRepository extends JpaRepository<Filiere, Integer> {
    Optional<Filiere> findByNom(String nom);
}