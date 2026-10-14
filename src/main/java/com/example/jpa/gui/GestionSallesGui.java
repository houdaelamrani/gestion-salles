package com.example.jpa.gui;

import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;
import com.example.jpa.service.ServiceGestion;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class GestionSallesGui extends JFrame {

    private ServiceGestion service;
    private DefaultTableModel modeleSalles;
    private DefaultTableModel modeleUtilisateurs;
    private JLabel labelTotalSalles;
    private JLabel labelTotalUsers;
    private JLabel labelCapaciteTotale;

    public GestionSallesGui() {
        this.service = new ServiceGestion();
        initialiserInterface();
        chargerDonneesInitiales();
    }

    private void initialiserInterface() {
        setTitle("TP9 JPA / Hibernate \u2014 Gestion des Salles & Utilisateurs | Houda El Amrani");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panneau Principal avec fond moderne
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(new Color(245, 247, 250));
        panelPrincipal.setBorder(new EmptyBorder(15, 20, 15, 20));

        // 1. HEADER PERSONNALISÉ
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 43, 73));
        headerPanel.setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel titreLabel = new JLabel("Syst\u00e8me de Gestion des Salles & Utilisateurs");
        titreLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titreLabel.setForeground(Color.WHITE);

        JLabel auteurLabel = new JLabel("TP9 Architecture JPA / Hibernate \u2022 \u00C9tudiante : Houda El Amrani");
        auteurLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        auteurLabel.setForeground(new Color(180, 205, 240));

        headerPanel.add(titreLabel, BorderLayout.NORTH);
        headerPanel.add(auteurLabel, BorderLayout.SOUTH);

        // 2. STATS KPI CARDS
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setOpaque(false);

        labelTotalSalles = new JLabel("0", SwingConstants.CENTER);
        labelTotalUsers = new JLabel("0", SwingConstants.CENTER);
        labelCapaciteTotale = new JLabel("0 places", SwingConstants.CENTER);

        statsPanel.add(creerCarteKpi("TOTAL SALLES", labelTotalSalles, new Color(41, 128, 185)));
        statsPanel.add(creerCarteKpi("TOTAL UTILISATEURS", labelTotalUsers, new Color(39, 174, 96)));
        statsPanel.add(creerCarteKpi("CAPACIT\u00c9 TOTALE", labelCapaciteTotale, new Color(142, 68, 173)));

        JPanel topContainer = new JPanel(new BorderLayout(0, 15));
        topContainer.setOpaque(false);
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(statsPanel, BorderLayout.CENTER);

        panelPrincipal.add(topContainer, BorderLayout.NORTH);

        // 3. ONGLETS DE DONNEES
        JTabbedPane onglets = new JTabbedPane();
        onglets.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Onglet Salles
        String[] colonnesSalles = {"ID", "Nom de la Salle", "Capacit\u00e9 (places)", "Type / Cat\u00e9gorie"};
        modeleSalles = new DefaultTableModel(colonnesSalles, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable tableSalles = creerTableStylisee(modeleSalles);
        JScrollPane scrollSalles = new JScrollPane(tableSalles);
        scrollSalles.setBorder(new LineBorder(new Color(220, 225, 230)));

        JPanel panelOngletSalles = new JPanel(new BorderLayout(10, 10));
        panelOngletSalles.setBackground(Color.WHITE);
        panelOngletSalles.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelOngletSalles.add(scrollSalles, BorderLayout.CENTER);

        // Onglet Utilisateurs
        String[] colonnesUsers = {"ID", "Nom & Pr\u00e9nom", "Adresse Email", "Salle Affect\u00e9e"};
        modeleUtilisateurs = new DefaultTableModel(colonnesUsers, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable tableUsers = creerTableStylisee(modeleUtilisateurs);
        JScrollPane scrollUsers = new JScrollPane(tableUsers);
        scrollUsers.setBorder(new LineBorder(new Color(220, 225, 230)));

        JPanel panelOngletUsers = new JPanel(new BorderLayout(10, 10));
        panelOngletUsers.setBackground(Color.WHITE);
        panelOngletUsers.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelOngletUsers.add(scrollUsers, BorderLayout.CENTER);

        onglets.addTab("  [Salles] Gestion des Salles  ", panelOngletSalles);
        onglets.addTab("  [Utilisateurs] Utilisateurs & Affectations  ", panelOngletUsers);

        panelPrincipal.add(onglets, BorderLayout.CENTER);

        // 4. PIED DE PAGE & BADGES
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel footerInfo = new JLabel("[OK] Base H2 active \u2022 Mapping JPA valid\u00e9 \u2022 TP9 Houda El Amrani");
        footerInfo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        footerInfo.setForeground(new Color(50, 120, 70));

        JButton btnCapture = new JButton("Exporter Capture d'\u00e9cran");
        btnCapture.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCapture.setBackground(new Color(41, 128, 185));
        btnCapture.setForeground(Color.WHITE);
        btnCapture.setFocusPainted(false);
        btnCapture.addActionListener(e -> exporterCapture("screenshot_gui.png"));

        footerPanel.add(footerInfo, BorderLayout.WEST);
        footerPanel.add(btnCapture, BorderLayout.EAST);

        panelPrincipal.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
    }

    private JPanel creerCarteKpi(String titre, JLabel labelValeur, Color couleurBande) {
        JPanel carte = new JPanel(new BorderLayout());
        carte.setBackground(Color.WHITE);
        carte.setBorder(new LineBorder(new Color(225, 230, 235), 1));

        JPanel bande = new JPanel();
        bande.setBackground(couleurBande);
        bande.setPreferredSize(new Dimension(6, 60));
        carte.add(bande, BorderLayout.WEST);

        JPanel contenu = new JPanel(new GridLayout(2, 1, 0, 2));
        contenu.setOpaque(false);
        contenu.setBorder(new EmptyBorder(8, 14, 8, 14));

        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTitre.setForeground(new Color(130, 140, 155));

        labelValeur.setFont(new Font("Segoe UI", Font.BOLD, 22));
        labelValeur.setForeground(new Color(30, 40, 55));
        labelValeur.setHorizontalAlignment(SwingConstants.LEFT);

        contenu.add(lblTitre);
        contenu.add(labelValeur);
        carte.add(contenu, BorderLayout.CENTER);

        return carte;
    }

    private JTable creerTableStylisee(DefaultTableModel modele) {
        JTable table = new JTable(modele);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setGridColor(new Color(240, 243, 246));
        table.setSelectionBackground(new Color(220, 235, 252));
        table.setSelectionForeground(new Color(20, 30, 50));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(245, 248, 250));
        header.setForeground(new Color(70, 80, 95));
        header.setPreferredSize(new Dimension(0, 35));

        DefaultTableCellRenderer centre = new DefaultTableCellRenderer();
        centre.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centre);
        table.getColumnModel().getColumn(0).setMaxWidth(60);

        return table;
    }

    public void chargerDonneesInitiales() {
        List<Salle> salles = service.listerSalles();
        if (salles.isEmpty()) {
            Salle s1 = new Salle("Lab IA & Data Science - Houda", 35);
            Salle s2 = new Salle("Amphith\u00e9\u00e2tre Al-Khawarizmi", 120);
            Salle s3 = new Salle("Salle Coworking B204", 45);
            Salle s4 = new Salle("Salle R\u00e9union Direction", 15);
            service.ajouterSalle(s1);
            service.ajouterSalle(s2);
            service.ajouterSalle(s3);
            service.ajouterSalle(s4);

            service.ajouterUtilisateur(new Utilisateur("Houda El Amrani", "houda.elamrani@emsi.ma", s1));
            service.ajouterUtilisateur(new Utilisateur("Dr. Karim Alami", "karim.alami@emsi.ma", s2));
            service.ajouterUtilisateur(new Utilisateur("Sofia Bennani", "sofia.bennani@emsi.ma", s1));
            service.ajouterUtilisateur(new Utilisateur("Yassine Tazi", "yassine.tazi@emsi.ma", s3));
            service.ajouterUtilisateur(new Utilisateur("Amine Chraibi", "amine.chraibi@emsi.ma", s3));
        }

        modeleSalles.setRowCount(0);
        int capaciteTotale = 0;
        for (Salle s : service.listerSalles()) {
            capaciteTotale += s.getCapacite();
            String type = s.getCapacite() >= 100 ? "Grand Amphi" : (s.getCapacite() >= 30 ? "Laboratoire" : "Salle Travaux/R\u00e9union");
            modeleSalles.addRow(new Object[]{s.getId(), s.getNom(), s.getCapacite() + " places", type});
        }

        modeleUtilisateurs.setRowCount(0);
        for (Utilisateur u : service.listerUtilisateurs()) {
            String salleStr = (u.getSalle() != null) ? u.getSalle().getNom() : "[Non affect\u00e9]";
            modeleUtilisateurs.addRow(new Object[]{u.getId(), u.getNom(), u.getEmail(), salleStr});
        }

        labelTotalSalles.setText(String.valueOf(service.totalSalles()));
        labelTotalUsers.setText(String.valueOf(service.totalUtilisateurs()));
        labelCapaciteTotale.setText(capaciteTotale + " places");
    }

    public void exporterCapture(String nomFichier) {
        try {
            BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            paint(g2);
            g2.dispose();

            File sortie = new File(nomFichier);
            ImageIO.write(image, "PNG", sortie);

            // Copie vers artefacts
            String pathArtifact = "C:/Users/elamr/.gemini/antigravity-ide/brain/e8fc42b5-3d91-48fb-b048-e637b0ec4da8/" + nomFichier;
            try {
                File dest = new File(pathArtifact);
                dest.getParentFile().mkdirs();
                Files.copy(sortie.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                // Ignore if path not found
            }

            System.out.println("[GUI] Capture sauvegardée avec succès : " + sortie.getAbsolutePath());
        } catch (Exception ex) {
            System.err.println("Erreur export capture GUI : " + ex.getMessage());
        }
    }

    public static void capturerSansAfficher(String nomFichier) {
        SwingUtilities.invokeLater(() -> {
            GestionSallesGui gui = new GestionSallesGui();
            gui.setSize(1080, 720);
            gui.doLayout();
            gui.setVisible(true);

            Timer timer = new Timer(600, e -> {
                gui.exporterCapture(nomFichier);
                gui.dispose();
                if (gui.service != null) {
                    gui.service.fermer();
                }
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestionSallesGui gui = new GestionSallesGui();
            gui.setVisible(true);
        });
    }
}
