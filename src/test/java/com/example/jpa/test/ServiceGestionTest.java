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
        // Nettoyage préalable pour garantir l'isolation et la reproductibilité exacte de chaque test
        service.nettoyerBase();
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
        Salle salle = new Salle("Salle Test", 20);
        service.ajouterSalle(salle);

        List<Salle> salles = service.listerSalles();
        assertFalse(salles.isEmpty());
        assertEquals("Salle Test", salles.get(0).getNom());
    }

    @Test
    public void testCreationUtilisateurAvecSalle() {
        Salle salle = new Salle("Salle B", 15);
        service.ajouterSalle(salle);

        Utilisateur utilisateur = new Utilisateur("Alice", "alice@example.com", salle);
        service.ajouterUtilisateur(utilisateur);

        List<Utilisateur> utilisateurs = service.listerUtilisateurs();
        assertFalse(utilisateurs.isEmpty());
        assertEquals("Alice", utilisateurs.get(0).getNom());
        assertEquals("Salle B", utilisateurs.get(0).getSalle().getNom());
    }

    @Test
    public void testRechercheUtilisateursParSalle() {
        Salle salle = new Salle("Salle C", 30);
        service.ajouterSalle(salle);

        Utilisateur u1 = new Utilisateur("Bob", "bob@example.com", salle);
        Utilisateur u2 = new Utilisateur("Carol", "carol@example.com", salle);
        service.ajouterUtilisateur(u1);
        service.ajouterUtilisateur(u2);

        List<Utilisateur> utilisateurs = service.utilisateursParSalle("Salle C");
        assertEquals(2, utilisateurs.size());
    }

    @Test
    public void testSallesParCapaciteMin() {
        Salle sPetite = new Salle("Petite-" + suffixe, 10);
        Salle sGrande = new Salle("Grande-" + suffixe, 50);

        service.ajouterSalle(sPetite);
        service.ajouterSalle(sGrande);

        List<Salle> grandes = service.sallesParCapaciteMin(40);

        assertTrue(grandes.stream().anyMatch(s -> s.getNom().equals(sGrande.getNom())));
        assertFalse(grandes.stream().anyMatch(s -> s.getNom().equals(sPetite.getNom())));
    }

    @Test
    public void testRechercheAvanceeJPQL() {
        Salle s1 = new Salle("Salle Réunion A", 15);
        Salle s2 = new Salle("Grand Amphi B", 100);
        service.ajouterSalle(s1);
        service.ajouterSalle(s2);

        // Test JPQL : capacité > 20
        List<Salle> grandesSalles = service.sallesParCapaciteSuperieure(20);
        assertEquals(1, grandesSalles.size());
        assertEquals("Grand Amphi B", grandesSalles.get(0).getNom());

        // Test JPQL : utilisateur LIKE '%Jean%'
        Utilisateur u1 = new Utilisateur("Jean Dupont", "jean@example.com", s1);
        Utilisateur u2 = new Utilisateur("Houda El Amrani", "houda@example.com", s2);
        service.ajouterUtilisateur(u1);
        service.ajouterUtilisateur(u2);

        List<Utilisateur> resultatNom = service.utilisateursParNomPattern("%Jean%");
        assertEquals(1, resultatNom.size());
        assertEquals("Jean Dupont", resultatNom.get(0).getNom());
    }

    @Test
    public void testModificationEtSuppression() {
        Salle salle = new Salle("Modif-" + suffixe, 15);
        service.ajouterSalle(salle);

        salle.setCapacite(25);
        Salle modifiee = service.modifierSalle(salle);
        assertEquals(25, modifiee.getCapacite());

        Long idSalle = salle.getId();
        boolean supprime = service.supprimerSalleParId(idSalle);
        assertTrue(supprime);
        assertNull(service.trouverSalle(idSalle));
    }

    @Test
    public void testAffectationUtilisateur() {
        Salle salle = new Salle("SalleAff-" + suffixe, 30);
        service.ajouterSalle(salle);

        Utilisateur u = new Utilisateur("SansSalle", "sans-" + suffixe + "@example.com");
        service.ajouterUtilisateur(u);
        assertNull(u.getSalle());

        boolean affecte = service.affecterUtilisateurASalle(u.getId(), salle.getId());
        assertTrue(affecte);

        Utilisateur uRecharge = service.trouverUtilisateur(u.getId());
        assertNotNull(uRecharge.getSalle());
        assertEquals(salle.getNom(), uRecharge.getSalle().getNom());
    }
}
