package org.example.gschool.repository;

import org.example.gschool.entity.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FiliereRepository extends JpaRepository<Filiere, Integer> {
    // Changé en Integer
}