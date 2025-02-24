package org.example.gschool.entity;

public class Connexion {
    private String email;
    private String password;

    // Constructeur avec deux paramètres
    public Connexion(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters et setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
