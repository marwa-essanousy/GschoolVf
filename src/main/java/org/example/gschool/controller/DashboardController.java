package org.example.gschool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping
    public String showDashboard() {
        return "dashboard"; // Retourne le fichier dashboard.html
    }
}
