package com.example.jpa.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CaptureResultat {

    /**
     * Génère une capture d'écran haute définition stylisée (style Terminal / IDE moderne)
     * représentant le résultat de l'exécution du TP9 pour Houda El Amrani.
     */
    public static void genererCaptureTerminal(List<String> lignes, String cheminSortie) {
        int largeur = 1120;
        int hauteurLigne = 23;
        int paddingH = 35;
        int paddingV = 75;
        int hauteur = Math.max(780, paddingV + (lignes.size() * hauteurLigne) + 40);

        BufferedImage image = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();

        // Anti-aliasing pour rendu ultra net
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Fond sombre moderne (palette Dark Slate / GitHub Dark)
        g2.setColor(new Color(22, 27, 34));
        g2.fillRect(0, 0, largeur, hauteur);

        // Barre de titre de la fenêtre (style Terminal / macOS)
        g2.setColor(new Color(33, 38, 45));
        g2.fillRect(0, 0, largeur, 46);

        // Boutons de contrôle de fenêtre (rouge, jaune, vert)
        g2.setColor(new Color(248, 81, 73));
        g2.fillOval(20, 16, 14, 14);
        g2.setColor(new Color(227, 179, 65));
        g2.fillOval(42, 16, 14, 14);
        g2.setColor(new Color(46, 160, 67));
        g2.fillOval(64, 16, 14, 14);

        // Titre dans la barre
        g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g2.setColor(new Color(201, 209, 217));
        String titreBarre = "Terminal \u2014 TP9 JPA / Hibernate : Gestion des Salles \u2014 \u00C9tudiante: Houda El Amrani";
        FontMetrics fm = g2.getFontMetrics();
        int titreX = (largeur - fm.stringWidth(titreBarre)) / 2;
        g2.drawString(titreBarre, titreX, 29);

        // Badge Statut
        g2.setColor(new Color(35, 134, 54));
        g2.fillRoundRect(largeur - 155, 11, 135, 24, 12, 12);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        // Dessin du check vectoriel
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(largeur - 143, 23, largeur - 139, 28);
        g2.drawLine(largeur - 139, 28, largeur - 133, 17);
        g2.drawString("BUILD SUCCESS", largeur - 128, 27);

        // Bordure inférieure de la barre d'onglets
        g2.setColor(new Color(48, 54, 61));
        g2.drawLine(0, 46, largeur, 46);

        // Police pour le contenu du terminal
        Font policeCode = new Font("Consolas", Font.PLAIN, 14);
        Font policeBold = new Font("Consolas", Font.BOLD, 14);
        g2.setFont(policeCode);

        int y = paddingV;

        for (String ligne : lignes) {
            // Nettoyage de caractères incompatibles pour le terminal
            String ligneAffichee = ligne.replace("✔", "[+]").replace("→", "->").replace("►", ">");

            // Palette de couleurs contextuelle selon le type de message
            if (ligneAffichee.contains("===") || ligneAffichee.contains("---")) {
                g2.setColor(new Color(110, 118, 129));
                g2.setFont(policeBold);
            } else if (ligneAffichee.contains("TP 9") || ligneAffichee.contains("Houda El Amrani") || ligneAffichee.startsWith(">>>")) {
                g2.setColor(new Color(88, 166, 255)); // Bleu clair GitHub
                g2.setFont(policeBold);
            } else if (ligneAffichee.contains("[OK]") || ligneAffichee.contains("Succes") || ligneAffichee.contains("SUCCES") || ligneAffichee.contains("SUCC\u00C8S")) {
                g2.setColor(new Color(63, 185, 80)); // Vert succès
                g2.setFont(policeBold);
            } else if (ligneAffichee.contains("[JPQL]") || ligneAffichee.contains("SELECT") || ligneAffichee.contains("places")) {
                g2.setColor(new Color(227, 179, 65)); // Jaune / Or
                g2.setFont(policeCode);
            } else if (ligneAffichee.contains("Connecte") || ligneAffichee.contains("Connect\u00e9") || ligneAffichee.contains("R\u00c9CAPITULATIF")) {
                g2.setColor(new Color(210, 168, 255)); // Violet
                g2.setFont(policeBold);
            } else if (ligneAffichee.startsWith("  |") || ligneAffichee.startsWith("  +")) {
                g2.setColor(new Color(240, 246, 252)); // Blanc tableau
                g2.setFont(policeCode);
            } else if (ligneAffichee.startsWith("  [+]")) {
                g2.setColor(new Color(126, 231, 135)); // Vert clair pour éléments listés
                g2.setFont(policeCode);
            } else {
                g2.setColor(new Color(201, 209, 217)); // Gris clair standard
                g2.setFont(policeCode);
            }

            g2.drawString(ligneAffichee, paddingH, y);
            y += hauteurLigne;
        }

        g2.dispose();

        try {
            File fichierSortie = new File(cheminSortie);
            File parent = fichierSortie.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            ImageIO.write(image, "PNG", fichierSortie);
            System.out.println("[Capture] Image sauvegard\u00e9e avec succ\u00e8s dans : " + fichierSortie.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erreur lors de la g\u00e9n\u00e9ration de la capture : " + e.getMessage());
        }
    }
}
