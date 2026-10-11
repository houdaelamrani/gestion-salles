
package com.example.jpa.test;

import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;
import com.example.jpa.service.ServiceGestion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class ServiceGestionTest {

    private ServiceGestion service;
    private String suffixe;

    @Before
    public void setUp() {
        service = new ServiceGestion();
        suffixe = UUID.randomUUID().toString().substring(0, 8);
    }

    @After
    public void tearDown() {
        if (service != null) {
            service.fermer();
        }
    }

    @Test
    public void testCreationEtRechercheSalle() {
        Salle salle = new Salle("Salle-" + suffixe, 20);

        service.ajouterSalle(salle);

        List<Salle> salles = service.listerSalles();

        assertNotNull(salle.getId());
        assertTrue(salles.stream()
                .anyMatch(s -> s.getNom().equals(salle.getNom())));
    }

    @Test
    public void testCreationUtilisateurAvecSalle() {
        Salle salle = new Salle("Salle-" + suffixe, 30);
        service.ajouterSalle(salle);

        Utilisateur utilisateur = new Utilisateur(
                "Utilisateur Test",
                "test-" + suffixe + "@example.com",
                salle
        );

        service.ajouterUtilisateur(utilisateur);

        assertNotNull(utilisateur.getId());

        List<Utilisateur> utilisateurs = service.listerUtilisateurs();

        assertTrue(utilisateurs.stream()
                .anyMatch(u -> u.getEmail().equals(utilisateur.getEmail())));
    }

    @Test
    public void testRechercheUtilisateursParSalle() {
        Salle salle = new Salle("Salle-" + suffixe, 25);
        service.ajouterSalle(salle);

        Utilisateur u1 = new Utilisateur(
                "Alice", "alice-" + suffixe + "@example.com", salle);
        Utilisateur u2 = new Utilisateur(
                "Bob", "bob-" + suffixe + "@example.com", salle);

        service.ajouterUtilisateur(u1);
        service.ajouterUtilisateur(u2);

        List<Utilisateur> resultats =
                service.utilisateursParSalle(salle.getNom());

        assertEquals(2, resultats.size());
    }
}
