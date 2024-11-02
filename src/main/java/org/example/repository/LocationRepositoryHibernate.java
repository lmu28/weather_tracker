package org.example.repository;

import org.example.model.Location;
import org.example.model.User;
import org.example.util.HibernateUtil;

import javax.persistence.EntityManager;
import java.util.Optional;

public class LocationRepositoryHibernate implements LocationRepository{
    @Override
    public void save(Location location) {
        EntityManager entityManager = HibernateUtil.getEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(location);
            entityManager.getTransaction().commit();
        }catch (RuntimeException e){
            entityManager.getTransaction().rollback();
        }finally {
            entityManager.close();
        }


    }

    @Override
    public Optional<Location> findByName(String name) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        Location location = null;
        try {
            entityManager.getTransaction().begin();
           location = (Location) entityManager.createQuery("select l from Location l where name = :name").
                    setParameter("name", name).getSingleResult();
            entityManager.getTransaction().commit();
        }catch (RuntimeException e){
            entityManager.getTransaction().rollback();
        }finally {
            entityManager.close();
        }
        return Optional.ofNullable(location);
    }

    @Override
    public void remove(Location location) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            location = entityManager.find(Location.class, location.getId());
            entityManager.remove(location);
            entityManager.getTransaction().commit();
        }catch (RuntimeException e){
            entityManager.getTransaction().rollback();
        }finally {
            entityManager.close();
        }

    }

    //    @Override
//    public int deleteByUser(User user) {
//
//        EntityManager entityManager = HibernateUtil.getEntityManager();
//        try {
//            entityManager.getTransaction().begin();
//
//            entityManager.createQuery("delete from ")
//
//
//
//
//            entityManager.getTransaction().commit();
//
//
//        }catch (RuntimeException e){
//            entityManager.getTransaction().rollback();
//        }finally {
//            entityManager.close();
//        }
//
//
//
//    }
}
