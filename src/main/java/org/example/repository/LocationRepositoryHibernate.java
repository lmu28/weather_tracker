package org.example.repository;

import org.example.model.User;
import org.example.util.HibernateUtil;

import javax.persistence.EntityManager;

public class LocationRepositoryHibernate implements LocationRepository{

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
