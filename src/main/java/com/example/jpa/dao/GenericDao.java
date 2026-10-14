package com.example.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class GenericDao<T> {

    private final Class<T> entityClass;
    protected EntityManager em;

    public GenericDao(Class<T> entityClass, EntityManager em) {
        this.entityClass = entityClass;
        this.em = em;
    }

    // CREATE : ajouter une entité
    public void create(T entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(entity);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    // READ : rechercher par identifiant
    public T find(Object id) {
        return em.find(entityClass, id);
    }

    // UPDATE : modifier une entité
    public T update(T entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T merged = em.merge(entity);
            tx.commit();
            return merged;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    // DELETE : supprimer une entité
    public void delete(T entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T managed = em.contains(entity) ? entity : em.merge(entity);
            em.remove(managed);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    // DELETE BY ID : supprimer une entité par son identifiant
    public boolean deleteById(Object id) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
                tx.commit();
                return true;
            }
            tx.commit();
            return false;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    // READ : récupérer toutes les entités
    public List<T> findAll() {
        return em.createQuery(
                "SELECT e FROM " + entityClass.getSimpleName() + " e",
                entityClass
        ).getResultList();
    }

    // COUNT : compter le nombre total d'entités
    public long count() {
        return em.createQuery(
                "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e",
                Long.class
        ).getSingleResult();
    }
}
