package org.example.gschool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
@Controller
public class accueilController {
    @GetMapping ("/")
    public String redirigerVersAccueil() {
        return "/accueill";
    }


}
