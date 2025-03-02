package org.example.gschool.controller;

import org.example.gschool.entity.Etudiant;
import org.example.gschool.entity.Filiere;
import org.example.gschool.service.EtudiantService;
import org.example.gschool.service.FiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/etudiants")
public class EtudiantController {

    private final EtudiantService etudiantService;
    private final FiliereService filiereService;


    public EtudiantController(EtudiantService etudiantService, FiliereService filiereService) {
        this.etudiantService = etudiantService;
        this.filiereService = filiereService;
    }


    @GetMapping
    public String listEtudiants(Model model) {
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        model.addAttribute("etudiants", etudiants);
        return "etudiants"; // Return the full view name, not the fragment
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        model.addAttribute("filieres", filiereService.getAllFilieres()); // Pass filières to model
        return "add-etudiant";
    }

    @PostMapping("/add")
    public String addEtudiant(@ModelAttribute Etudiant etudiant, @RequestParam Integer filiereId, RedirectAttributes redirectAttributes) {
        Optional<Filiere> filiereOptional = filiereService.getFiliereById(filiereId);

        if (!filiereOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "La filière sélectionnée n'existe pas.");
            return "redirect:/etudiants/add";
        }

        Filiere filiere = filiereOptional.get();
        etudiant.setFiliere(filiere);
        etudiantService.saveEtudiant(etudiant);

        return "redirect:/etudiants";
    }

    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Etudiant etudiant = etudiantService.getEtudiantById(id);
        if (etudiant == null) return "redirect:/etudiants";
        model.addAttribute("etudiant", etudiant);
        model.addAttribute("filieres", filiereService.getAllFilieres());
        return "update-etudiant";
    }


    @PostMapping("/update/{id}")
    public String updateEtudiant(@PathVariable Integer id, @ModelAttribute Etudiant etudiant) {
        etudiant.setId(id);
        etudiantService.saveEtudiant(etudiant);
        return "redirect:/etudiants";
    }

    @GetMapping("/delete/{id}")
    public String deleteEtudiant(@PathVariable Integer id) {
        etudiantService.deleteEtudiantById(id);
        return "redirect:/etudiants";
    }
}
