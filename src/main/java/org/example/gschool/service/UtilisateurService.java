package org.example.gschool.service;

import org.example.gschool.entity.Utilisateur;
import org.example.gschool.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public Optional<Utilisateur> login(String email, String motDePasse) {
        return utilisateurRepository.findByEmailAndMotDePasse(email, motDePasse);
    }
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        utilisateur.setMotDePasse(encoder.encode(utilisateur.getMotDePasse()));
        utilisateurRepository.save(utilisateur);

    }
    public List<Utilisateur> listeUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public void supprimerUtilisateur(Integer id) {
        utilisateurRepository.deleteById(id);
    }

    public Utilisateur getUtilisateurById(Integer id) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);
        return utilisateur.orElse(null);
    }

    public void modifierUtilisateur(Utilisateur utilisateur) {
        utilisateurRepository.save(utilisateur);
    }

    public void saveUtilisateur(Utilisateur utilisateur) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        utilisateur.setMotDePasse(encoder.encode(utilisateur.getMotDePasse()));
        utilisateurRepository.save(utilisateur);
    }


}
