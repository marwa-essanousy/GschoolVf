package org.example.gschool.repository;

import jakarta.persistence.EntityManager;
import jakarta.websocket.Session;
import org.example.gschool.entity.Etudiant;
import org.example.gschool.entity.Filiere;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {

    @Query("SELECT e FROM Etudiant e WHERE " +
            "(:name IS NULL OR e.nom LIKE %:name%) AND " +
            "(:email IS NULL OR e.email LIKE %:email%) AND " +
            "(:code IS NULL OR e.codeEtudiant LIKE %:code%) " +
            "ORDER BY " +
            "CASE WHEN :sort = 'nom' THEN e.nom END ASC, " +
            "CASE WHEN :sort = 'nom_desc' THEN e.nom END DESC, " +
            "CASE WHEN :sort = 'email' THEN e.email END ASC, " +
            "CASE WHEN :sort = 'email_desc' THEN e.email END DESC, " +
            "CASE WHEN :sort = 'code' THEN e.codeEtudiant END ASC, " +
            "CASE WHEN :sort = 'code_desc' THEN e.codeEtudiant END DESC")
    List<Etudiant> searchEtudiants(@Param("name") String name,
                                   @Param("email") String email,
                                   @Param("code") String code,
                                   @Param("sort") String sort);

    int countByFiliere(Filiere filiere);


    @Query("SELECT e.filiere.nom, COUNT(e) FROM Etudiant e GROUP BY e.filiere")
    List<Object[]> countStudentsByFiliere();
}