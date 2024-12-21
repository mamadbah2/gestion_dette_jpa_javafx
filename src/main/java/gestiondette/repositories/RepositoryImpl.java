package gestiondette.repositories;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import gestiondette.core.Repository;

public abstract class RepositoryImpl<T> implements Repository<T> {
    protected Class<T> entityClass;

    protected EntityManager entityManager;
    protected EntityManagerFactory emf;

    public RepositoryImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
        // Utilisez une méthode statique pour éviter de créer plusieurs fois l'EMF
        this.emf = PersistenceUnitManager.getEntityManagerFactory();
    }

    // Méthode utilitaire pour obtenir un nouvel EntityManager
    protected EntityManager getEntityManager() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("gestiondette");
        }
        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = emf.createEntityManager();
        }
        return entityManager;
    }

    @Override
    public void insert(T entity) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            if (!em.contains(entity)) {
                em.merge(entity);
            }
            em.persist(entity);
            em.flush();
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Insert failed", e);
        }
    }

    @Override
    public T selectById(int id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(entityClass, id);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<T> selectAll() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.createQuery("FROM " + entityClass.getSimpleName(), entityClass).getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void update(T entity) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = getEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            em.merge(entity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Update failed", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(T entity) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = getEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            // Assurez-vous que l'entité est gérée
            if (!em.contains(entity)) {
                entity = em.merge(entity);
            }
            em.remove(entity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Delete failed", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // Classe utilitaire pour gérer l'EntityManagerFactory
    private static class PersistenceUnitManager {
        private static EntityManagerFactory INSTANCE;

        public static synchronized EntityManagerFactory getEntityManagerFactory() {
            if (INSTANCE == null) {
                INSTANCE = Persistence.createEntityManagerFactory("gestiondette");
            }
            return INSTANCE;
        }

        public static void closeEntityManagerFactory() {
            if (INSTANCE != null && INSTANCE.isOpen()) {
                INSTANCE.close();
            }
        }
    }

    // Méthode de nettoyage globale
    public static void cleanup() {
        PersistenceUnitManager.closeEntityManagerFactory();
    }
}