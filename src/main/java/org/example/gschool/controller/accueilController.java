package org.example.gschool.controller;

import org.example.gschool.entity.Utilisateur;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class accueilController {
    @GetMapping ("/")
    public String redirigerVersAccueil(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String username = authentication.getName();
            String email = ((Utilisateur) authentication.getPrincipal()).getEmail();

            model.addAttribute("userName", username);
            model.addAttribute("userEmail", email);
        }



        return "accueill.html";
    }


}