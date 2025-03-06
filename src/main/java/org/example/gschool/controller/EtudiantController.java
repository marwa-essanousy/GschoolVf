package org.example.gschool.controller;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.gschool.entity.Etudiant;
import org.example.gschool.entity.Filiere;
import org.example.gschool.service.EtudiantService;
import org.example.gschool.service.FiliereService;
import org.springframework.beans.factory.annotation.Autowired;
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
        etudiantService.updateEtudiant(existingEtudiant);
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



        @GetMapping("/downloadPDF")
        public void downloadPdf(HttpServletResponse response) throws Exception {
            // Configurer le type de réponse
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=etudiants.pdf");

            // Création du document PDF
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(response.getOutputStream()));
            Document document = new Document(pdfDoc);

            // Ajouter un titre au PDF
            document.add(new Paragraph("Liste des Étudiants").setBold().setFontSize(16));

            // Ajouter un tableau avec des colonnes pour les données
            float[] columnWidths = {1, 3, 3, 4, 2, 3, 2}; // Largeur des colonnes
            Table table = new Table(columnWidths);

            // En-tête du tableau
            table.addCell(new Cell().add(new Paragraph("ID")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addCell(new Cell().add(new Paragraph("Nom")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addCell(new Cell().add(new Paragraph("Prénom")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addCell(new Cell().add(new Paragraph("Email")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addCell(new Cell().add(new Paragraph("Date de Naissance")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addCell(new Cell().add(new Paragraph("Filière")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addCell(new Cell().add(new Paragraph("Code Étudiant")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

            // Récupérer les étudiants depuis le service
            List<Etudiant> etudiants = etudiantService.getAllEtudiants();
            System.out.println("Nombre d'étudiants récupérés : " + etudiants.size());

            if (etudiants.isEmpty()) {
                // Message si aucun étudiant
                document.add(new Paragraph("⚠ Aucun étudiant pour l’instant !"));
            } else {
                // Remplir les données du tableau
                for (Etudiant etudiant : etudiants) {
                    table.addCell(new Cell().add(new Paragraph(etudiant.getId() != null ? etudiant.getId().toString() : "-")));
                    table.addCell(new Cell().add(new Paragraph(etudiant.getNom() != null ? etudiant.getNom() : "-")));
                    table.addCell(new Cell().add(new Paragraph(etudiant.getPrenom() != null ? etudiant.getPrenom() : "-")));
                    table.addCell(new Cell().add(new Paragraph(etudiant.getEmail() != null ? etudiant.getEmail() : "-")));
                    table.addCell(new Cell().add(new Paragraph(etudiant.getDateNaissance() != null ? etudiant.getDateNaissance().toString() : "-")));
                    table.addCell(new Cell().add(new Paragraph(etudiant.getFiliere() != null ? etudiant.getFiliere().getNom() : "-")));
                    table.addCell(new Cell().add(new Paragraph(etudiant.getCodeEtudiant() != null ? etudiant.getCodeEtudiant() : "-")));
                }
                document.add(table); // Ajouter le tableau au document
            }

            document.close(); // Fermer le document
        }


    @GetMapping("/downloadEXCEL")
    public void downloadExcel(HttpServletResponse response) throws IOException {
        // Configurer la réponse HTTP
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=etudiants.xlsx");

        // Créer un nouveau classeur Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Étudiants");

        // Créer l'en-tête du tableau
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nom");
        headerRow.createCell(2).setCellValue("Prénom");
        headerRow.createCell(3).setCellValue("Email");
        headerRow.createCell(4).setCellValue("Date de Naissance");
        headerRow.createCell(5).setCellValue("Filière");
        headerRow.createCell(6).setCellValue("Code Étudiant");

        // Récupérer les étudiants depuis le service
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();

        // Remplir les données du tableau
        int rowNum = 1;
        for (Etudiant etudiant : etudiants) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(etudiant.getId() != null ? etudiant.getId() : 0);
            row.createCell(1).setCellValue(etudiant.getNom() != null ? etudiant.getNom() : "");
            row.createCell(2).setCellValue(etudiant.getPrenom() != null ? etudiant.getPrenom() : "");
            row.createCell(3).setCellValue(etudiant.getEmail() != null ? etudiant.getEmail() : "");
            row.createCell(4).setCellValue(etudiant.getDateNaissance() != null ? etudiant.getDateNaissance().toString() : "");
            row.createCell(5).setCellValue(etudiant.getFiliere() != null ? etudiant.getFiliere().getNom() : "");
            row.createCell(6).setCellValue(etudiant.getCodeEtudiant() != null ? etudiant.getCodeEtudiant() : "");
        }

        // Écrire le classeur dans la réponse HTTP
        workbook.write(response.getOutputStream());
        workbook.close();
    }
    }



