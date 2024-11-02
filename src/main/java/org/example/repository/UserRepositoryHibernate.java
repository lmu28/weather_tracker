package org.example.repository;

import org.example.model.Location;
import org.example.model.User;
import org.example.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Optional;

public class UserRepositoryHibernate implements UserRepository {


    @Override
    public Optional<User> findByName(String username) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        User user = null;
        try {
            entityManager.getTransaction().begin();
            user = (User) entityManager.createQuery("select u from User u where u.name = :name")
                    .setParameter("name", username).getSingleResult();
            entityManager.getTransaction().commit();
        }catch (RuntimeException e){
            entityManager.getTransaction().rollback();
        }finally {
            entityManager.close();
        }
        return Optional.ofNullable(user);
    }

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

    @Override
    public User save(User transientUser) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(transientUser);
            entityManager.getTransaction().commit();
        }catch (RuntimeException e){
            entityManager.getTransaction().rollback();
            return null;
        }finally {
            entityManager.close();
        }
        return transientUser;


    }


    @Override
    public void removeLocationFromUser(User user, Location location) {

        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            user = entityManager.find(User.class, user.getId());
            user.getLocations().remove(location);
            entityManager.getTransaction().commit();
        }catch (RuntimeException e){
            entityManager.getTransaction().rollback();
        }finally {
            entityManager.close();
        }

    }


    @Override
    public User addLocationToUser(User user, Location location) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            user = entityManager.find(User.class, user.getId());
            user.getLocations().add(location);
            entityManager.getTransaction().commit();
        }catch (RuntimeException e){
            entityManager.getTransaction().rollback();
            return null;
        }finally {
            entityManager.close();
        }
        return user;

    }
}
