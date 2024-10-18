package org.example.repository;

import org.example.model.Session;
import org.example.service.SessionService;
import org.example.util.HibernateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;



import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class SessionRepositoryHibernateTest {


    private final static String  ID1 = "s1";
    private SessionService sessionService = new SessionService();

    @BeforeEach
    void setUp() {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("RUNSCRIPT FROM 'classpath:/init.sql'").executeUpdate();
        entityManager.getTransaction().commit();

        }catch (RuntimeException e){
            entityManager.getTransaction().rollback();
        }finally {
            entityManager.close();
        }

    }

    @Test
    void findById_sessionExist_returnsValidSession() {
        Session session  = sessionService.get(ID1);
        assertThat(session).isInstanceOf(Session.class);
        assertThat(session).matches(s -> s.getId().equals(ID1));
    }

    @Test
    void findById_sessionNotExists_ReturnsNull() {
        String notValidId = "null";
        Session session  = sessionService.get(notValidId);
        assertThat(session).isNull();
        assertThat(sessionService.get(null)).isNull();
        assertThat(sessionService.get("")).isNull();
    }


}