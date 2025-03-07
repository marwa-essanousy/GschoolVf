package org.example.gschool.controller.api;

import org.example.gschool.service.FiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class FiliereApiController {

    private final FiliereService filiereService;

    @Autowired
    public FiliereApiController(FiliereService filiereService) {
        this.filiereService = filiereService;
    }

    @GetMapping("/stats")
    public Map<String, Integer> getStats() {
        return filiereService.getNombreEtudiantsParFiliere();
    }
}
