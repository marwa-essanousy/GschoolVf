package org.example.gschool.controller;

import org.example.gschool.entity.Filiere;
import org.example.gschool.service.FiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/filieres")
public class filiereController {

    @Autowired
    private FiliereService filiereService;

    @GetMapping
    public String listFilieres(Model model) {
        model.addAttribute("filieres", filiereService.getAllFilieres());
        return "filiere";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("filiere", new Filiere());
        return "add-filiere";
    }

    @PostMapping("/save")
    public String saveFiliere(@ModelAttribute Filiere filiere) {
        filiereService.saveFiliere(filiere);
        return "redirect:/filieres";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("filiere", filiereService.getFiliereById(id).orElseThrow(() -> new IllegalArgumentException("Invalid filiere Id:" + id)));
        return "edit-filiere";
    }

    @PostMapping("/update/{id}")
    public String updateFiliere(@PathVariable Integer id, @ModelAttribute Filiere filiere) {
        filiere.setId(id);
        filiereService.saveFiliere(filiere);
        return "redirect:/filieres";
    }

    @GetMapping("/delete/{id}")
    public String deleteFiliere(@PathVariable Integer id) {
        filiereService.deleteFiliere(id);
        return "redirect:/filieres";
    }
}