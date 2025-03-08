package org.example.gschool.controller;

import org.example.gschool.entity.Utilisateur;
import org.example.gschool.service.EtudiantService;
import org.example.gschool.service.FiliereService;
import org.example.gschool.service.UtilisateurService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final EtudiantService etudiantService;
    private final FiliereService filiereService;
    private final UtilisateurService utilisateurService;

    public DashboardController(EtudiantService etudiantService, FiliereService filiereService, UtilisateurService utilisateurService) {
        this.etudiantService = etudiantService;
        this.filiereService = filiereService;
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public String getDashboard(Model model) {
        // Récupérer les informations de l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String username = authentication.getName();  // Nom d'utilisateur
            String email = ((Utilisateur) authentication.getPrincipal()).getEmail();

            // Ajouter les informations de l'utilisateur au modèle
            model.addAttribute("userName", username);
            model.addAttribute("userEmail", email);
        }

        // Dataset 1: Nombre d'étudiants par filière
        List<Object[]> studentCountsByFiliere = etudiantService.getStudentCountByFiliere();
        String[] filieres = new String[studentCountsByFiliere.size()];
        Integer[] nombreEtudiants = new Integer[studentCountsByFiliere.size()];

        for (int i = 0; i < studentCountsByFiliere.size(); i++) {
            filieres[i] = (String) studentCountsByFiliere.get(i)[0]; // Nom de la filière
            nombreEtudiants[i] = ((Long) studentCountsByFiliere.get(i)[1]).intValue(); // Nombre d'étudiants
        }

        // Récupérer les comptes pour les étudiants, filières et utilisateurs
        long etudiantCount = etudiantService.getAllEtudiants().size();
        long filiereCount = filiereService.getAllFilieres().size();
        long utilisateurCount = utilisateurService.listeUtilisateurs().size();

        // Ajouter les données au modèle
        model.addAttribute("filieres", filieres);
        model.addAttribute("nombreEtudiants", nombreEtudiants);
        model.addAttribute("etudiantCount", etudiantCount);
        model.addAttribute("filiereCount", filiereCount);
        model.addAttribute("utilisateurCount", utilisateurCount);

        return "dashboard"; // Le nom de votre template Thymeleaf
    }
}