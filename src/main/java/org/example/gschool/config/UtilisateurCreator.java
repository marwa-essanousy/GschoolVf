package org.example.gschool.config;
import org.example.gschool.entity.Utilisateur;
import org.example.gschool.repository.UtilisateurRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(scanBasePackages = "org.example.gschool")
public class UtilisateurCreator {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(UtilisateurCreator.class, args);
        UtilisateurRepository utilisateurRepository = context.getBean(UtilisateurRepository.class);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("marwa@gmail.com");
        utilisateur.setMotDePasse(encoder.encode("marwa2003"));
        utilisateur.setNom("marwa elkamari");
        utilisateur.setPrenom("es");
        utilisateur.setRole("admin");

        utilisateurRepository.save(utilisateur);
        System.out.println("Utilisateur enregistré avec succès!");
}
}