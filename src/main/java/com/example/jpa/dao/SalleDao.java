package com.example.jpa.dao;

import com.example.jpa.entities.Salle;
import javax.persistence.EntityManager;
import java.util.List;

public class SalleDao extends GenericDao<Salle> {

    public SalleDao(EntityManager em) {
        super(Salle.class, em);
    }

    public List<Salle> findByCapaciteMin(int capaciteMin) {
        return em.createQuery(
                        "SELECT s FROM Salle s WHERE s.capacite >= :capaciteMin ORDER BY s.capacite DESC",
                        Salle.class
                )
                .setParameter("capaciteMin", capaciteMin)
                .getResultList();
    }

    public List<Salle> findByCapaciteSuperieure(int minCapacite) {
        return em.createQuery(
                        "SELECT s FROM Salle s WHERE s.capacite > :minCapacite ORDER BY s.capacite DESC",
                        Salle.class
                )
                .setParameter("minCapacite", minCapacite)
                .getResultList();
    }

    public Salle findByNom(String nom) {
        List<Salle> results = em.createQuery(
                        "SELECT s FROM Salle s WHERE s.nom = :nom",
                        Salle.class
                )
                .setParameter("nom", nom)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public List<Salle> findByCapaciteBetween(int min, int max) {
        return em.createQuery(
                        "SELECT s FROM Salle s WHERE s.capacite BETWEEN :min AND :max ORDER BY s.capacite ASC",
                        Salle.class
                )
                .setParameter("min", min)
                .setParameter("max", max)
                .getResultList();
    }
}
