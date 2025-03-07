package org.example.gschool.service;

import org.example.gschool.entity.Filiere;
import org.example.gschool.repository.EtudiantRepository;
import org.example.gschool.repository.FiliereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FiliereService {

    private final EtudiantRepository etudiantRepository;
    private final FiliereRepository filiereRepository;

    @Autowired
    public FiliereService(EtudiantRepository etudiantRepository, FiliereRepository filiereRepository) {
        this.etudiantRepository = etudiantRepository;
        this.filiereRepository = filiereRepository;
    }

    public List<Filiere> getAllFilieres() {
        return filiereRepository.findAll();
    }

    public Optional<Filiere> getFiliereById(Integer id) {
        return filiereRepository.findById(id);
    }

    public Filiere saveFiliere(Filiere filiere) {
        return filiereRepository.save(filiere);
    }

    public void deleteFiliere(Integer id) {
        filiereRepository.deleteById(id);
    }

    public Map<String, Integer> getNombreEtudiantsParFiliere() {
        List<Filiere> filieres = filiereRepository.findAll();
        Map<String, Integer> stats = new HashMap<>();

        for (Filiere filiere : filieres) {
            int count = etudiantRepository.countByFiliere(filiere);
            stats.put(filiere.getNom(), count);
        }

        return stats;
    }
}
