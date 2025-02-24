package org.example.gschool.service;

import org.example.gschool.entity.Connexion;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AuthService {

    // Liste des comptes autorisés (simulée ici)
    private final List<Connexion> authorizedAccounts = Arrays.asList(
            new Connexion("admin1@example.com", "password1"),
            new Connexion("admin2@example.com", "password2")
    );

    public boolean authenticate(String email, String password) {
        return authorizedAccounts.stream()
                .anyMatch(account -> account.getEmail().equals(email) && account.getPassword().equals(password));
    }
}