
package com.example.jpa;

import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;
import com.example.jpa.service.ServiceGestion;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        ServiceGestion service = new ServiceGestion();

        try {
            // 1. Creer une salle
            Salle salle = new Salle("Salle Informatique", 30);
            service.ajouterSalle(salle);

            System.out.println("=== Liste des salles ===");

            // 2. Afficher toutes les salles
            List<Salle> salles = service.listerSalles();

            for (Salle s : salles) {
                System.out.println(s);
            }

            // 3. Creer un utilisateur dans cette salle
            Utilisateur utilisateur = new Utilisateur(
                    "Houda",
                    "houda.test@example.com",
                    salle
            );

            service.ajouterUtilisateur(utilisateur);

            System.out.println("\n=== Liste des utilisateurs ===");

            // 4. Afficher tous les utilisateurs
            List<Utilisateur> utilisateurs =
                    service.listerUtilisateurs();

            for (Utilisateur u : utilisateurs) {
                System.out.println(u);
            }

        } finally {
            // 5. Fermer les ressources
            service.fermer();
        }
    }
}
