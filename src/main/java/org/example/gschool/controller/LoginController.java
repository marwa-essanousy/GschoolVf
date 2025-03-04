package org.example.gschool.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.gschool.entity.Utilisateur;
import org.example.gschool.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping
    public String showLoginPage() {
        return "login"; // Affiche login.html
    }

    @PostMapping
    public String login(@RequestParam("email") String email,
                        @RequestParam("motDePasse") String motDePasse,
                        Model model) {

        Optional<Utilisateur> utilisateur = utilisateurService.login(email, motDePasse);

        if (utilisateur.isPresent()) {
            // Ajoutez l'utilisateur dans le modèle pour la session ou redirection
            model.addAttribute("utilisateur", utilisateur.get());
            return "redirect:/"; // Redirige vers le tableau de bord
        } else {
            // Affiche une erreur si les identifiants sont incorrects
            model.addAttribute("error", "Email ou mot de passe incorrect");
            return "login"; // Retourne à la page login.html
        }
    }

    @GetMapping("/deconnecter")
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Récupère la session si elle existe
        if (session != null) {
            session.invalidate(); // Invalide la session
        }
        return "redirect:/accueil"; // Redirige vers la page de connexion
    }

}
