package org.example.gschool.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.gschool.entity.Utilisateur;
import org.example.gschool.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/utilisateurs/ajouter")
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String username = authentication.getName();
            String email = ((Utilisateur) authentication.getPrincipal()).getEmail();

            model.addAttribute("userName", username);
            model.addAttribute("userEmail", email);
        }
        return "utilisateurs";
    }
    @PostMapping
    public String login(@RequestParam("email") String email,
                        @RequestParam("motDePasse") String motDePasse,
                        HttpServletRequest request,
                        Model model) {

        Optional<Utilisateur> utilisateur = utilisateurService.login(email, motDePasse);

        if (utilisateur.isPresent()) {
            HttpSession session = request.getSession();
            session.setAttribute("utilisateur", utilisateur.get());
            return "redirect:/"; // Redirige vers le tableau de bord
        } else {
            model.addAttribute("error", "Email ou mot de passe incorrect");
            return "login"; // Retourne à la page login.html
        }
    }

    @GetMapping("/utilisateurs/supprimer/{id}")
    public String supprimerUtilisateur(@PathVariable Integer id) {
        utilisateurService.supprimerUtilisateur(id);
        return "redirect:/utilisateurs";
    }



    @GetMapping("/utilisateurs/modifier/{id}")
    public String modifierUtilisateur(@PathVariable Integer id, Model model) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);
        model.addAttribute("utilisateur", utilisateur);
        return "modifier-utilisateur";
    }

    @PostMapping("/utilisateurs/modifier/{id}")
    public String modifierUtilisateur(@PathVariable Integer id, @ModelAttribute Utilisateur utilisateur) {
        utilisateurService.modifierUtilisateur(utilisateur);
        return "redirect:/utilisateurs";
    }

}

