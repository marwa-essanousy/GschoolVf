package org.example.gschool.controller;

import org.example.gschool.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class ConnexionController {

    @Autowired
    private AuthService authService;

    // Afficher la page de connexion
    @GetMapping("/connexion")
    public String loginForm() {
        return "connexion"; // Affiche la page connexion.html
    }

    // Traiter la soumission du formulaire de connexion
    @PostMapping("/connexion")
    public String login(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        if (authService.authenticate(email, password)) {
            session.setAttribute("adminLoggedIn", true); // Stocker l'état de connexion dans la session
            return "redirect:/filieres"; // Redirige vers la page des filières
        } else {
            model.addAttribute("error", "Email ou mot de passe incorrect");
            return "connexion"; // Affiche à nouveau la page de connexion avec un message d'erreur
        }
    }

    // Gérer la déconnexion
    @GetMapping("/deconnexion")
    public String logout(HttpSession session) {
        session.removeAttribute("adminLoggedIn"); // Supprimer l'état de connexion
        return "redirect:/connexion"; // Rediriger vers la page de connexion
    }
}