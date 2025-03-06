package org.example.gschool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class accueilController {
    @GetMapping ("/")
    public String redirigerVersAccueil() {
        return "accueill.html";
    }


}