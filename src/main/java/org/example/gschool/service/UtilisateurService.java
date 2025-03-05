package org.example.gschool.service;

import org.example.gschool.entity.Utilisateur;
import org.example.gschool.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public Optional<Utilisateur> login(String email, String motDePasse) {
        return utilisateurRepository.findByEmailAndMotDePasse(email, motDePasse);
    }
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateurRepository.save(utilisateur);
    }
    public List<Utilisateur> listeUtilisateurs() {
        return utilisateurRepository.findAll();
    }
}
