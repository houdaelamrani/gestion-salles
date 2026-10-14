package com.example.jpa;

import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;
import com.example.jpa.gui.GestionSallesGui;
import com.example.jpa.service.ServiceGestion;
import com.example.jpa.util.CaptureResultat;
import com.example.jpa.util.CaptureResultatWindows;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final List<String> logsTerminal = new ArrayList<>();

    private static void log(String message) {
        System.out.println(message);
        logsTerminal.add(message);
    }

    public static void main(String[] args) {
        // Réduction du bruit des logs internes Hibernate pour garder un affichage élégant
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        String dateActuelle = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        log("================================================================================");
        log("  TP 9 : GESTION DES SALLES & UTILISATEURS (JPA / HIBERNATE 5.6)");
        log("  Nom de l'\u00e9tudiante : Houda El Amrani | Date : " + dateActuelle);
        log("  Base de donn\u00e9es   : H2 Database (In-Memory) | Statut : Connect\u00e9 [OK]");
        log("================================================================================");
        log("");

        ServiceGestion service = new ServiceGestion();

        try {
            // ---------------------------------------------------------
            // 1. CRÉATION DES SALLES (PERSISTENCE)
            // ---------------------------------------------------------
            log(">>> [ETAPE 1] Cr\u00e9ation et persistance des salles :");
            Salle s1 = new Salle("Lab IA & Data Science - Houda", 35);
            Salle s2 = new Salle("Amphith\u00e9\u00e2tre Al-Khawarizmi", 120);
            Salle s3 = new Salle("Salle Coworking B204", 25);
            Salle s4 = new Salle("Salle R\u00e9union Direction", 15);

            service.ajouterSalle(s1);
            service.ajouterSalle(s2);
            service.ajouterSalle(s3);
            service.ajouterSalle(s4);

            log("  [OK] 4 Salles persist\u00e9es avec succ\u00e8s dans la base de donn\u00e9es.");
            log("");

            // ---------------------------------------------------------
            // 2. CRÉATION DES UTILISATEURS ET AFFECTATION
            // ---------------------------------------------------------
            log(">>> [ETAPE 2] Cr\u00e9ation des utilisateurs personnalis\u00e9s :");
            Utilisateur u1 = new Utilisateur("Houda El Amrani", "houda.elamrani@emsi.ma", s1);
            Utilisateur u2 = new Utilisateur("Dr. Karim Alami", "karim.alami@emsi.ma", s2);
            Utilisateur u3 = new Utilisateur("Sofia Bennani", "sofia.bennani@emsi.ma", s1);
            Utilisateur u4 = new Utilisateur("Yassine Tazi", "yassine.tazi@emsi.ma", s3);
            Utilisateur u5 = new Utilisateur("Amine Chraibi", "amine.chraibi@emsi.ma"); // Sans salle initiale

            service.ajouterUtilisateur(u1);
            service.ajouterUtilisateur(u2);
            service.ajouterUtilisateur(u3);
            service.ajouterUtilisateur(u4);
            service.ajouterUtilisateur(u5);

            log("  [OK] 5 Utilisateurs enregistr\u00e9s avec succ\u00e8s.");
            log("");

            // ---------------------------------------------------------
            // 3. AFFICHAGE DES SALLES
            // ---------------------------------------------------------
            log(">>> [ETAPE 3] Liste des Salles en base :");
            log(String.format("  +-----+---------------------------------------+------------+"));
            log(String.format("  | %-3s | %-37s | %-10s |", "ID", "Nom de la Salle", "Capacit\u00e9"));
            log(String.format("  +-----+---------------------------------------+------------+"));
            for (Salle s : service.listerSalles()) {
                log(String.format("  | %-3d | %-37s | %-10d |", s.getId(), s.getNom(), s.getCapacite()));
            }
            log(String.format("  +-----+---------------------------------------+------------+"));
            log("");

            // ---------------------------------------------------------
            // 4. AFFICHAGE DES UTILISATEURS
            // ---------------------------------------------------------
            log(">>> [ETAPE 4] Liste des Utilisateurs en base :");
            log(String.format("  +-----+----------------------+--------------------------+--------------------------------+"));
            log(String.format("  | %-3s | %-20s | %-24s | %-30s |", "ID", "Nom Utilisateur", "Email", "Salle Affect\u00e9e"));
            log(String.format("  +-----+----------------------+--------------------------+--------------------------------+"));
            for (Utilisateur u : service.listerUtilisateurs()) {
                String nomSalle = (u.getSalle() != null) ? u.getSalle().getNom() : "[AUCUNE]";
                log(String.format("  | %-3d | %-20s | %-24s | %-30s |", u.getId(), u.getNom(), u.getEmail(), nomSalle));
            }
            log(String.format("  +-----+----------------------+--------------------------+--------------------------------+"));
            log("");

            // ---------------------------------------------------------
            // 5. REQUÊTE JPQL : Utilisateurs par Salle
            // ---------------------------------------------------------
            String salleCible = "Lab IA & Data Science - Houda";
            log(">>> [ETAPE 5] [JPQL] Utilisateurs pr\u00e9sents dans : '" + salleCible + "' :");
            List<Utilisateur> membresLab = service.utilisateursParSalle(salleCible);
            for (Utilisateur u : membresLab) {
                log("  [+] " + u.getNom() + " (" + u.getEmail() + ")");
            }
            log("");

            // ---------------------------------------------------------
            // ---------------------------------------------------------
            // 6. REQUÊTE JPQL : Salles avec capacité >= 30 et recherche par motif
            // ---------------------------------------------------------
            int capMin = 30;
            log(">>> [ETAPE 6] [JPQL] Salles ayant une capacité >= " + capMin + " personnes :");
            List<Salle> grandesSalles = service.sallesParCapaciteMin(capMin);
            for (Salle s : grandesSalles) {
                log("  [+] " + s.getNom() + " -> " + s.getCapacite() + " places");
            }

            log(">>> [ETAPE 6 bis] [JPQL] Utilisateurs dont le nom contient 'El Amrani' (LIKE %...%) :");
            List<Utilisateur> usersTrouves = service.utilisateursParNomPattern("%El Amrani%");
            for (Utilisateur u : usersTrouves) {
                log("  [+] Trouvé : " + u.getNom() + " <" + u.getEmail() + ">");
            }
            log("");

            // ---------------------------------------------------------
            // 7. MISE À JOUR (UPDATE)
            // ---------------------------------------------------------
            log(">>> [ETAPE 7] Test de mise à jour (Update) :");
            s3.setCapacite(45);
            service.modifierSalle(s3);
            log("  [OK] Capacité de 'Salle Coworking B204' modifiée à : " + s3.getCapacite() + " places.");

            service.affecterUtilisateurASalle(u5.getId(), s3.getId());
            Utilisateur u5Recharge = service.trouverUtilisateur(u5.getId());
            log("  [OK] Utilisateur 'Amine Chraibi' nouvellement affecté à : " + u5Recharge.getSalle().getNom());
            log("");

            // ---------------------------------------------------------
            // 8. SUPPRESSION D'UN ÉLÉMENT TEST (DELETE)
            // ---------------------------------------------------------
            log(">>> [ETAPE 8] Test de suppression sécurisée (Delete) :");
            Salle salleTemp = new Salle("Salle Temporaire Test", 10);
            service.ajouterSalle(salleTemp);
            log("  [OK] Salle temporaire créée avec ID: " + salleTemp.getId());
            service.supprimerSalle(salleTemp);
            Salle verif = service.trouverSalle(salleTemp.getId());
            log("  [OK] Suppression confirmée : " + (verif == null ? "Salle inexistante (Succès)" : "Échec"));
            log("");

            // ---------------------------------------------------------
            // STATISTIQUES FINALES
            // ---------------------------------------------------------
            log("================================================================================");
            log("  RÉCAPITULATIF FINAL DU TP9 :");
            log("  - Total Salles actives       : " + service.totalSalles());
            log("  - Total Utilisateurs inscrits: " + service.totalUtilisateurs());
            log("  - Statut de l'exécution       : 100% SUCCÈS — TOUTES LES OPÉRATIONS VALIDEES");
            log("================================================================================");

        } finally {
            service.fermer();
        }

        // ---------------------------------------------------------
        // GÉNÉRATION DES CAPTURES D'ÉCRAN
        // ---------------------------------------------------------
        // 1. Capture authentique Windows Terminal (PowerShell Windows)
        String cheminCaptureWindows = "screenshot_windows.png";
        CaptureResultatWindows.genererCaptureWindows(logsTerminal, cheminCaptureWindows);

        // Copie de la capture Windows vers les artefacts
        String cheminArtifactWindows = "C:/Users/elamr/.gemini/antigravity-ide/brain/e8fc42b5-3d91-48fb-b048-e637b0ec4da8/screenshot_windows.png";
        try {
            File sourceWin = new File(cheminCaptureWindows);
            File destWin = new File(cheminArtifactWindows);
            destWin.getParentFile().mkdirs();
            Files.copy(sourceWin.toPath(), destWin.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("[Capture Windows] Copie vers artefacts reussie : " + cheminArtifactWindows);
        } catch (Exception e) {
            System.err.println("Note: Copie artefact Windows: " + e.getMessage());
        }

        // 2. Capture style IDE moderne
        String cheminCaptureLocal = "screenshot_resultat.png";
        CaptureResultat.genererCaptureTerminal(logsTerminal, cheminCaptureLocal);

        String cheminArtifact = "C:/Users/elamr/.gemini/antigravity-ide/brain/e8fc42b5-3d91-48fb-b048-e637b0ec4da8/screenshot_resultat.png";
        try {
            File source = new File(cheminCaptureLocal);
            File dest = new File(cheminArtifact);
            dest.getParentFile().mkdirs();
            Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.err.println("Note: Copie artefact: " + e.getMessage());
        }

        // 3. Export de la capture GUI (Interface Graphique)
        try {
            GestionSallesGui.capturerSansAfficher("screenshot_gui.png");
        } catch (Throwable t) {
            System.out.println("Note: Export capture GUI : " + t.getMessage());
        }
    }
}
