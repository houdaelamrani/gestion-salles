package com.example.jpa.dao;

import com.example.jpa.entities.Utilisateur;
import javax.persistence.EntityManager;
import java.util.List;

public class UtilisateurDao extends GenericDao<Utilisateur> {

    public UtilisateurDao(EntityManager em) {
        super(Utilisateur.class, em);
    }

    public List<Utilisateur> findBySalleNom(String nomSalle) {
        return em.createQuery(
                        "SELECT u FROM Utilisateur u WHERE u.salle.nom = :nomSalle ORDER BY u.nom ASC",
                        Utilisateur.class
                )
                .setParameter("nomSalle", nomSalle)
                .getResultList();
    }

    public Utilisateur findByEmail(String email) {
        List<Utilisateur> results = em.createQuery(
                        "SELECT u FROM Utilisateur u WHERE u.email = :email",
                        Utilisateur.class
                )
                .setParameter("email", email)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public List<Utilisateur> findByNomLike(String pattern) {
        return em.createQuery(
                        "SELECT u FROM Utilisateur u WHERE u.nom LIKE :pattern",
                        Utilisateur.class
                )
                .setParameter("pattern", pattern)
                .getResultList();
    }

    public List<Utilisateur> findSansSalle() {
        return em.createQuery(
                        "SELECT u FROM Utilisateur u WHERE u.salle IS NULL ORDER BY u.nom ASC",
                        Utilisateur.class
                )
                .getResultList();
    }
}
