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
import org.example.gschool.entity.Filiere;
import org.example.gschool.entity.Utilisateur;
import org.example.gschool.service.FiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/filieres")
public class filiereController {

    private FiliereService filiereService;

    public filiereController(FiliereService filiereService) {
        this.filiereService = filiereService;
    }

    @GetMapping
    public String listFilieres(Model model) {
        model.addAttribute("filieres", filiereService.getAllFilieres());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String username = authentication.getName();
            String email = ((Utilisateur) authentication.getPrincipal()).getEmail();

            model.addAttribute("userName", username);
            model.addAttribute("userEmail", email);
        }
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

    @GetMapping("/downloadPDFF")
    public void downloadPdfF(HttpServletResponse response) throws Exception {
        // Configurer le type de réponse
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=filiere.pdf");

        // Création du document PDF
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(response.getOutputStream()));
        Document document = new Document(pdfDoc);

        // Ajouter un titre au PDF
        document.add(new Paragraph("Liste des Filières").setBold().setFontSize(16));

        // Ajouter un tableau avec des colonnes pour les données
        float[] columnWidths = {1, 3, 4}; // Largeur des colonnes
        Table table = new Table(columnWidths);

        // En-tête du tableau
        table.addCell(new Cell().add(new Paragraph("ID")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addCell(new Cell().add(new Paragraph("Nom")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addCell(new Cell().add(new Paragraph("Description")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        // Récupérer les filières depuis le service
        List<Filiere> filieres = filiereService.getAllFilieres();
        System.out.println("Nombre de filières récupérées : " + filieres.size());

        if (filieres.isEmpty()) {
            // Message si aucune filière
            document.add(new Paragraph("⚠ Aucun filière pour l’instant !"));
        } else {
            // Remplir les données du tableau
            for (Filiere filiere : filieres) {
                table.addCell(new Cell().add(new Paragraph(filiere.getId() != null ? filiere.getId().toString() : "-")));
                table.addCell(new Cell().add(new Paragraph(filiere.getNom() != null ? filiere.getNom() : "-")));
                table.addCell(new Cell().add(new Paragraph(filiere.getDescription() != null ? filiere.getDescription() : "-")));
            }
            document.add(table); // Ajouter le tableau au document
        }

        document.close(); // Fermer le document
    }

    @GetMapping("/downloadEXCELF")
    @ResponseBody
    public void downloadExcelF(HttpServletResponse response) throws IOException {
        // Configurer la réponse HTTP
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=filieres.xlsx");

        // Créer un nouveau classeur Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Filières");

        // Créer l'en-tête du tableau
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nom");
        headerRow.createCell(2).setCellValue("Description");

        // Récupérer les filières depuis le service
        List<Filiere> filieres = filiereService.getAllFilieres();

        // Remplir les données du tableau
        int rowNum = 1;
        for (Filiere filiere : filieres) { Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(filiere.getId() != null ? filiere.getId() : 0);
            row.createCell(1).setCellValue(filiere.getNom() != null ? filiere.getNom() : "");
            row.createCell(2).setCellValue(filiere.getDescription() != null ? filiere.getDescription() : "");
        }

        // Écrire le classeur dans la réponse HTTP
        workbook.write(response.getOutputStream());
        workbook.close();
    }


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("filieres", filiereService.getAllFilieres());
        model.addAttribute("stats", filiereService.getNombreEtudiantsParFiliere());
        return "dashboard";
    }
}


