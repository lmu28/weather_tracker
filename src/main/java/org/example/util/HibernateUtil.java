package org.example.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

;

public class HibernateUtil {

    private static EntityManagerFactory factory;

    public static EntityManager getEntityManager(){
        if (factory == null){
            factory = Persistence.createEntityManagerFactory("factory");
        }
        EntityManager entityManager = factory.createEntityManager();
        return entityManager;

    }
}
