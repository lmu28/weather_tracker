package org.example.repository;

import org.example.model.User;
import org.example.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Optional;

public class UserRepositoryHibernate implements UserRepository {


    @Override
    public Optional<User> findByNamePassword(String name, String password) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        User user = null;
        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("select u from User u " +
                    "where u.name = :name and u.password = :password");
            query.setParameter("name", name);
            query.setParameter("password", password);

            user = ((User) query.getSingleResult());
            entityManager.getTransaction().commit();

        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }


        return Optional.ofNullable(user);
    }



}
