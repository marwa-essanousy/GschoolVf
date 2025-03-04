package org.example.gschool.service;

import org.example.gschool.entity.Etudiant;
import org.example.gschool.entity.Filiere;
import org.example.gschool.repository.EtudiantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;

    public EtudiantService(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
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
        return etudiantRepository.searchEtudiants(name, email, code,sort);}
}