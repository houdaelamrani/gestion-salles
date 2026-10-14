package com.example.jpa.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class CaptureResultatWindows {

    /**
     * Génère une capture d'écran authentique style Windows Terminal / PowerShell (Windows 11 / 10)
     * personnalisée pour Houda El Amrani - TP9 Gestion des Salles.
     */
    public static void genererCaptureWindows(List<String> logs, String cheminSortie) {
        int largeur = 1180;
        int hauteurLigne = 21;
        int paddingH = 25;
        int paddingV = 70;

        // Calcul de la hauteur totale requise
        int nbLignesCommandesEnTete = 4;
        int nbLignesCommandesFin = 2;
        int totalLignes = nbLignesCommandesEnTete + logs.size() + nbLignesCommandesFin;
        int hauteur = Math.max(820, paddingV + (totalLignes * hauteurLigne) + 30);

        BufferedImage image = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();

        // Rendu net & anti-aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Fond console Windows Terminal moderne (noir profond / bleu nuit très discret)
        g2.setColor(new Color(12, 12, 12));
        g2.fillRect(0, 0, largeur, hauteur);

        // ================= BARRE DE TITRE WINDOWS (WINDOWS 10/11) =================
        int hauteurBarreTitre = 38;
        g2.setColor(new Color(31, 31, 31));
        g2.fillRect(0, 0, largeur, hauteurBarreTitre);

        // Bordure fenêtre Windows subtile
        g2.setColor(new Color(55, 55, 55));
        g2.drawRect(0, 0, largeur - 1, hauteur - 1);
        g2.drawLine(0, hauteurBarreTitre, largeur, hauteurBarreTitre);

        // Onglet actif style Windows Terminal
        int ongletLargeur = 320;
        g2.setColor(new Color(12, 12, 12));
        g2.fillRect(6, 4, ongletLargeur, hauteurBarreTitre - 4);
        g2.setColor(new Color(0, 120, 215)); // Accent bleu Windows au-dessus de l'onglet actif
        g2.fillRect(6, 4, ongletLargeur, 2);

        // Icône Windows PowerShell (petit polygone bleu + chevron)
        g2.setColor(new Color(1, 36, 86));
        g2.fillRoundRect(16, 11, 18, 18, 4, 4);
        g2.setColor(new Color(78, 154, 241));
        g2.setFont(new Font("Consolas", Font.BOLD, 12));
        g2.drawString(">", 20, 24);

        // Titre dans l'onglet Windows Terminal
        g2.setColor(new Color(230, 230, 230));
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2.drawString("Windows PowerShell - [Houda El Amrani : TP9]", 40, 23);

        // Bouton "+" pour nouvel onglet
        g2.setColor(new Color(140, 140, 140));
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        g2.drawString("+", 336, 24);

        // ================= BOUTONS DE CONTRÔLE WINDOWS (À DROITE) =================
        // Bouton Minimiser (─)
        int btnWidth = 46;
        g2.setColor(new Color(200, 200, 200));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawLine(largeur - (btnWidth * 3) + 18, 19, largeur - (btnWidth * 3) + 28, 19);

        // Bouton Maximiser (▢)
        g2.drawRect(largeur - (btnWidth * 2) + 18, 14, 10, 10);

        // Bouton Fermer (✕)
        int closeX = largeur - btnWidth + 18;
        g2.drawLine(closeX, 14, closeX + 10, 24);
        g2.drawLine(closeX + 10, 14, closeX, 24);

        // Séparateurs de boutons discrets
        g2.setColor(new Color(45, 45, 45));
        g2.drawLine(largeur - (btnWidth * 3), 0, largeur - (btnWidth * 3), hauteurBarreTitre);

        // ================= CONTENU DU TERMINAL WINDOWS =================
        Font policeConsolas = new Font("Consolas", Font.PLAIN, 14);
        Font policeConsolasBold = new Font("Consolas", Font.BOLD, 14);
        g2.setFont(policeConsolas);

        int y = paddingV;

        // Entête PowerShell Windows standard
        g2.setColor(new Color(204, 204, 204));
        g2.drawString("Windows PowerShell", paddingH, y);
        y += hauteurLigne;
        g2.drawString("Copyright (C) Microsoft Corporation. Tous droits r\u00e9serv\u00e9s.", paddingH, y);
        y += hauteurLigne;
        g2.setColor(new Color(128, 128, 128));
        g2.drawString("Install the latest PowerShell for new features and improvements! https://aka.ms/PSWindows", paddingH, y);
        y += hauteurLigne + 6;

        // Commande exécutée par Houda
        String invite = "PS C:\\Users\\elamr\\gestion-salles> ";
        g2.setColor(new Color(245, 245, 245));
        g2.setFont(policeConsolasBold);
        g2.drawString(invite, paddingH, y);
        int largeurInvite = g2.getFontMetrics().stringWidth(invite);
        g2.setColor(new Color(249, 241, 165)); // Commande en jaune clair comme dans Windows Terminal
        g2.drawString("mvn clean compile exec:java", paddingH + largeurInvite, y);
        y += hauteurLigne + 4;

        // Lignes de résultat du projet
        g2.setFont(policeConsolas);
        for (String ligne : logs) {
            String l = ligne.replace("✔", "[+]").replace("→", "->");

            if (l.contains("===") || l.contains("---")) {
                g2.setColor(new Color(118, 118, 118));
                g2.setFont(policeConsolasBold);
            } else if (l.contains("TP 9") || l.contains("Houda El Amrani") || l.startsWith(">>>")) {
                g2.setColor(new Color(59, 142, 234)); // Bleu Windows Terminal
                g2.setFont(policeConsolasBold);
            } else if (l.contains("[OK]") || l.contains("SUCC\u00C8S") || l.contains("Succ\u00e8s") || l.contains("100%")) {
                g2.setColor(new Color(22, 198, 12)); // Vert fluo terminal
                g2.setFont(policeConsolasBold);
            } else if (l.contains("[JPQL]") || l.contains("SELECT") || l.contains("places")) {
                g2.setColor(new Color(249, 241, 165)); // Jaune console
                g2.setFont(policeConsolas);
            } else if (l.contains("Connect\u00e9") || l.contains("R\u00c9CAPITULATIF")) {
                g2.setColor(new Color(180, 130, 240)); // Violet
                g2.setFont(policeConsolasBold);
            } else if (l.startsWith("  |") || l.startsWith("  +")) {
                g2.setColor(new Color(245, 245, 245)); // Blanc pur tableau
                g2.setFont(policeConsolas);
            } else if (l.startsWith("  [+]")) {
                g2.setColor(new Color(97, 214, 214)); // Cyan pour listes
                g2.setFont(policeConsolas);
            } else {
                g2.setColor(new Color(204, 204, 204)); // Gris console standard
                g2.setFont(policeConsolas);
            }

            g2.drawString(l, paddingH, y);
            y += hauteurLigne;
        }

        // Ligne finale d'invite de commande avec curseur clignotant
        y += 8;
        g2.setColor(new Color(245, 245, 245));
        g2.setFont(policeConsolasBold);
        g2.drawString(invite, paddingH, y);
        int inviteX = paddingH + g2.getFontMetrics().stringWidth(invite);
        // Curseur Windows Terminal (rectangle plein)
        g2.setColor(new Color(245, 245, 245));
        g2.fillRect(inviteX + 2, y - 13, 8, 15);

        g2.dispose();

        // Sauvegarde de l'image
        try {
            File fichierSortie = new File(cheminSortie);
            File parent = fichierSortie.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            ImageIO.write(image, "PNG", fichierSortie);
            System.out.println("[Capture Windows] Image sauvegard\u00e9e : " + fichierSortie.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde capture Windows : " + e.getMessage());
        }
    }
}
