package org.example.gschool.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.gschool.entity.Etudiant;
import org.example.gschool.entity.Filiere;
import org.example.gschool.service.EtudiantService;
import org.example.gschool.service.FiliereService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/etudiants")
public class EtudiantController {

    private final EtudiantService etudiantService;
    private final FiliereService filiereService;
    private final Path uploadDir = Paths.get("C:\\Users\\HP\\Desktop\\uploads");

    public EtudiantController(EtudiantService etudiantService, FiliereService filiereService) {
        this.etudiantService = etudiantService;
        this.filiereService = filiereService;

        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create upload directory", e);
        }
    }

    @GetMapping
    public String listEtudiants(Model model) {
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        model.addAttribute("etudiants", etudiants);
        return "etudiants";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        model.addAttribute("filieres", filiereService.getAllFilieres());
        return "add-etudiant";
    }

    @PostMapping("/add")
    public String addEtudiant(
            @ModelAttribute Etudiant etudiant,
            @RequestParam Integer filiereId,
            @RequestParam("photoFile") MultipartFile photoFile,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Filiere> filiereOptional = filiereService.getFiliereById(filiereId);

        if (!filiereOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "La filière sélectionnée n'existe pas.");
            return "redirect:/etudiants/add";
        }

        if (!photoFile.isEmpty()) {
            try {
                String filename = photoFile.getOriginalFilename();
                Path filePath = uploadDir.resolve(filename);
                Files.copy(photoFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                etudiant.setPhoto(filename);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'upload de la photo.");
                return "redirect:/etudiants/add";
            }
        }

        etudiant.setFiliere(filiereOptional.get());
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
    public String updateEtudiant(
            @PathVariable Integer id,
            @ModelAttribute Etudiant etudiant,
            @RequestParam Integer filiereId,
            @RequestParam("photoFile") MultipartFile photoFile,
            RedirectAttributes redirectAttributes
    ) {
        // Récupérer l'étudiant existant
        Optional<Etudiant> existingEtudiantOptional = Optional.ofNullable(etudiantService.getEtudiantById(id));
        if (!existingEtudiantOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "L'étudiant à modifier n'existe pas.");
            return "redirect:/etudiants";
        }

        // Récupérer la filière sélectionnée
        Optional<Filiere> filiereOptional = filiereService.getFiliereById(filiereId);
        if (!filiereOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "La filière sélectionnée n'existe pas.");
            return "redirect:/etudiants";
        }

        Etudiant existingEtudiant = existingEtudiantOptional.get();

        // Mise à jour des informations de l'étudiant
        existingEtudiant.setCodeEtudiant(etudiant.getCodeEtudiant());
        existingEtudiant.setNom(etudiant.getNom());
        existingEtudiant.setPrenom(etudiant.getPrenom());
        existingEtudiant.setEmail(etudiant.getEmail());
        existingEtudiant.setDateNaissance(etudiant.getDateNaissance());
        existingEtudiant.setFiliere(filiereOptional.get());

        // Gestion de la photo
        if (!photoFile.isEmpty()) {
            try {
                // Supprimer l'ancienne photo si elle existe
                if (existingEtudiant.getPhoto() != null) {
                    Path oldFilePath = uploadDir.resolve(existingEtudiant.getPhoto());
                    Files.deleteIfExists(oldFilePath);
                }

                // Sauvegarder la nouvelle photo
                String filename = photoFile.getOriginalFilename();
                Path filePath = uploadDir.resolve(filename);
                Files.copy(photoFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                existingEtudiant.setPhoto(filename);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'upload de la photo.");
                return "redirect:/etudiants/update/" + id;
            }
        }

        // Sauvegarder les modifications
        etudiantService.saveEtudiant(existingEtudiant);
        redirectAttributes.addFlashAttribute("successMessage", "Étudiant modifié avec succès !");
        return "redirect:/etudiants";
    }


    @GetMapping("/delete/{id}")
    public String deleteEtudiant(@PathVariable Integer id) {
        etudiantService.deleteEtudiantById(id);
        return "redirect:/etudiants";
    }

    @GetMapping("/uploads/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = uploadDir.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/search")
    public String searchEtudiants(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "sort", required = false) String sort,
            Model model,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

        List<Etudiant> etudiants = etudiantService.searchEtudiants(name, email, code, sort);
        model.addAttribute("etudiants", etudiants);

        if ("XMLHttpRequest".equals(requestedWith)) {
            return "fragments/etudiant/etudiants :: #etudiantsBody";
        }

        return "etudiants";
    }





}