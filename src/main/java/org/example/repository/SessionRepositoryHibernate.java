package org.example.repository;

import org.example.model.Session;
import org.example.model.User;
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

    @Override
    public Session save(Session transientSession) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(transientSession);
            entityManager.getTransaction().commit();
        }catch (RuntimeException e){
            entityManager.getTransaction().rollback();
            return null;
        }finally {
            entityManager.close();
        }
        return transientSession;

    }

    @Override
    public int deleteById(String id) {
        EntityManager entityManager = HibernateUtil.getEntityManager();

        try {
            entityManager.getTransaction().begin();
            int deleted = entityManager.createQuery("delete from Session s where s.id = :id").
                    setParameter("id", id).
                    executeUpdate();

            entityManager.getTransaction().commit();
            return deleted;
        }catch (RuntimeException e){
            entityManager.getTransaction().rollback();
        }finally {
            entityManager.close();
        }

        return 0;
    }
}
