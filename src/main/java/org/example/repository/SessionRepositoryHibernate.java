package org.example.repository;

import org.example.model.Session;
import org.example.util.HibernateUtil;


import javax.persistence.EntityManager;
import java.util.Optional;

public class SessionRepositoryHibernate implements SessionRepository {


    @Override
    public Optional<Session> findById(String id) {
        EntityManager entityManager = HibernateUtil.getEntityManager();

        Session session = null;
        try {
            entityManager.getTransaction().begin();
            session = entityManager.find(Session.class, id);
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }

        return Optional.ofNullable(session);
    }
}
