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
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

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
    public List<Filiere> getAllFilieresWithStudentCount() {
        List<Filiere> filieres = filiereRepository.findAll();
        for (Filiere filiere : filieres) {
            int count = etudiantRepository.countByFiliere(filiere);
            filiere.setNombre_etudiant(count); // Remplir le champ nombre_etudiant
        }
        return filieres;
    }


    public Map<String, Integer> getNombreEtudiantsParFiliere() {
        List<Filiere> filieres = filiereRepository.findAll();
        Map<String, Integer> stats = new HashMap<>();

        // Remplir la map avec les données
        for (Filiere filiere : filieres) {
            int count = etudiantRepository.countByFiliere(filiere);
            stats.put(filiere.getNom(), count);
        }

        // Trier la map par valeur (nombre d'étudiants) en ordre décroissant
        return stats.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new // Pour conserver l'ordre de tri
                ));
    }
}
