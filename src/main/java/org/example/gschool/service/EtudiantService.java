package org.example.gschool.service;

import org.example.gschool.entity.Etudiant;
import org.example.gschool.repository.EtudiantRepository;
import org.example.gschool.repository.FiliereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final FiliereRepository filiereRepository;

    @Autowired
    public EtudiantService(EtudiantRepository etudiantRepository, FiliereRepository filiereRepository) {
        this.etudiantRepository = etudiantRepository;
        this.filiereRepository = filiereRepository;
    }

    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }

    public Etudiant getEtudiantById(Integer id) {
        return etudiantRepository.findById(id).orElse(null);
    }

    public void saveEtudiant(Etudiant etudiant) {
        etudiantRepository.save(etudiant);
    }

    public void updateEtudiant(Etudiant etudiant) {
        etudiantRepository.save(etudiant);
    }

    public void deleteEtudiantById(Integer id) {
        etudiantRepository.deleteById(id);
    }

    public List<Etudiant> searchEtudiants(String name, String email, String code, String sort) {
        return etudiantRepository.searchEtudiants(name, email, code, sort);
    }

    public List<Object[]> getStudentCountByFiliere() {
        return etudiantRepository.countStudentsByFiliere();
}
}


