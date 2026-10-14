package com.example.jpa.service;

import com.example.jpa.dao.SalleDao;
import com.example.jpa.dao.UtilisateurDao;
import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ServiceGestion {

    private EntityManagerFactory emf;
    private EntityManager em;

    private SalleDao salleDao;
    private UtilisateurDao utilisateurDao;

    public ServiceGestion() {
        this("gestionPU");
    }

    public ServiceGestion(String persistenceUnitName) {
        this.emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        this.em = emf.createEntityManager();
        this.salleDao = new SalleDao(em);
        this.utilisateurDao = new UtilisateurDao(em);
    }

    public ServiceGestion(EntityManager em) {
        this.emf = null;
        this.em = em;
        this.salleDao = new SalleDao(em);
        this.utilisateurDao = new UtilisateurDao(em);
    }

    // ==================== GESTION DES SALLES ====================

    public void ajouterSalle(Salle salle) {
        salleDao.create(salle);
    }

    public Salle trouverSalle(Long id) {
        return salleDao.find(id);
    }

    public Salle trouverSalleParNom(String nom) {
        return salleDao.findByNom(nom);
    }

    public Salle modifierSalle(Salle salle) {
        return salleDao.update(salle);
    }

    public void supprimerSalle(Salle salle) {
        salleDao.delete(salle);
    }

    public boolean supprimerSalleParId(Long id) {
        return salleDao.deleteById(id);
    }

    public List<Salle> listerSalles() {
        return salleDao.findAll();
    }

    public List<Salle> sallesParCapaciteMin(int capaciteMin) {
        return salleDao.findByCapaciteMin(capaciteMin);
    }

    public List<Salle> sallesParCapaciteSuperieure(int minCapacite) {
        return salleDao.findByCapaciteSuperieure(minCapacite);
    }

    public long totalSalles() {
        return salleDao.count();
    }

    // ==================== GESTION DES UTILISATEURS ====================

    public void ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateurDao.create(utilisateur);
    }

    public Utilisateur trouverUtilisateur(Long id) {
        return utilisateurDao.find(id);
    }

    public Utilisateur trouverUtilisateurParEmail(String email) {
        return utilisateurDao.findByEmail(email);
    }

    public Utilisateur modifierUtilisateur(Utilisateur utilisateur) {
        return utilisateurDao.update(utilisateur);
    }

    public void supprimerUtilisateur(Utilisateur utilisateur) {
        utilisateurDao.delete(utilisateur);
    }

    public boolean supprimerUtilisateurParId(Long id) {
        return utilisateurDao.deleteById(id);
    }

    public List<Utilisateur> listerUtilisateurs() {
        return utilisateurDao.findAll();
    }

    public List<Utilisateur> utilisateursParSalle(String nomSalle) {
        return utilisateurDao.findBySalleNom(nomSalle);
    }

    public List<Utilisateur> utilisateursParNomPattern(String pattern) {
        return utilisateurDao.findByNomLike(pattern);
    }

    public List<Utilisateur> utilisateursSansSalle() {
        return utilisateurDao.findSansSalle();
    }

    public long totalUtilisateurs() {
        return utilisateurDao.count();
    }

    public void nettoyerBase() {
        for (Utilisateur u : listerUtilisateurs()) {
            supprimerUtilisateur(u);
        }
        for (Salle s : listerSalles()) {
            supprimerSalle(s);
        }
    }

    // ==================== OPERATIONS METIER ====================

    public boolean affecterUtilisateurASalle(Long utilisateurId, Long salleId) {
        Utilisateur user = utilisateurDao.find(utilisateurId);
        Salle salle = salleDao.find(salleId);
        if (user != null && salle != null) {
            user.setSalle(salle);
            utilisateurDao.update(user);
            return true;
        }
        return false;
    }

    public boolean retirerUtilisateurDeSalle(Long utilisateurId) {
        Utilisateur user = utilisateurDao.find(utilisateurId);
        if (user != null) {
            user.setSalle(null);
            utilisateurDao.update(user);
            return true;
        }
        return false;
    }

    // ==================== FERMETURE DES RESSOURCES ====================

    public void fermer() {
        if (em != null && em.isOpen()) {
            em.close();
        }

        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}