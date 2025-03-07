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
    public class LoginController {

        @GetMapping("/login")
        public String showLoginPage() {
            return "login";
        }

        @PostMapping("/login")
        public String login(@RequestParam String email, @RequestParam String password) {
            return "redirect:/";
}


    }