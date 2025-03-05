package org.example.gschool.controller;

import org.example.gschool.entity.Utilisateur;
import org.example.gschool.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/add-utilisateur")
    public String addUtilisateur(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "add-utilisateur";
    }

    @PostMapping("/ajouter")
    public String ajouterUtilisateur(@ModelAttribute Utilisateur utilisateur) {
        utilisateurService.ajouterUtilisateur(utilisateur);
        return "redirect:/utilisateurs";
    }

    @GetMapping("/utilisateurs")
    public String listeUtilisateurs(Model model) {
        List<Utilisateur> utilisateurs = utilisateurService.listeUtilisateurs();
        model.addAttribute("utilisateurs", utilisateurs);
        return "utilisateurs";
    }

}

