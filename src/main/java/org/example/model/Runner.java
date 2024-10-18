package org.example.model;

import org.example.util.HibernateUtil;

import javax.persistence.EntityManager;

public class Runner {


    public static void main(String[] args) {


        EntityManager entityManager = HibernateUtil.getEntityManager();

        Session session = entityManager.find(Session.class, 1L);
        User user = entityManager.find(User.class, 1L);
        System.out.println(session);

        entityManager.close();


    }


}
