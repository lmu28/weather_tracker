package org.example.service;

import org.example.model.Session;
import org.example.model.User;
import org.example.repository.SessionRepositoryHibernate;
import org.example.repository.UserRepositoryHibernate;
import org.example.util.CryptUtil;
import org.example.util.HibernateUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Table;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;


class SessionServiceTestIT {


    private final static String ID1 = "session_1";
    public static final int SESSIONS_COUNT = 4;
    private SessionService sessionService = new SessionService(new SessionRepositoryHibernate());

    private UserService userService = new UserService(new UserRepositoryHibernate(), new CryptUtil());

    @BeforeAll
    static void beforeAll() {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("RUNSCRIPT FROM 'classpath:/init.sql'").executeUpdate();
            entityManager.getTransaction().commit();

        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }

    }

    @Test
    void get_sessionExist_returnsValidSession() {
        Session session = sessionService.get(ID1);
        assertThat(session).isInstanceOf(Session.class);
        assertThat(session).matches(s -> s.getId().equals(ID1));
    }

    @Test
    void get_sessionNotExists_returnsNull() {
        String notValidId = "null";
        Session session = sessionService.get(notValidId);
        assertThat(session).isNull();
        assertThat(sessionService.get(null)).isNull();
        assertThat(sessionService.get("")).isNull();
    }


    @Test
    void isValid_notExpired_returnsTrue() {
        Session session = sessionService.get(ID1);
        assertThat(sessionService.isValid(session)).isTrue();
    }

    @Test
    void isValid_expired_returnsFalse() {
        Session session = new Session();
        session.setExpiresAt(Timestamp.from(Instant.now().minus(Duration.ofHours(1))));
        assertThat(sessionService.isValid(session)).isFalse();
    }

    @Test
    void save() {
        User user = null;
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            user = entityManager.find(User.class, 4L);
            entityManager.getTransaction().commit();

        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }
        assertThat(user).isNotNull();

        entityManager = HibernateUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            List<Session> sessions = (List<Session>) entityManager.createQuery("select s from Session s ").getResultList();

            assertThat(sessions.size()).isEqualTo(SESSIONS_COUNT);
            Session session = sessionService.save(user);

            sessions = (List<Session>) entityManager.createQuery("select s from Session s ").getResultList();
            assertThat(sessions.size()).isEqualTo(SESSIONS_COUNT + 1);

            long userId = user.getId();
            assertThat(session).matches(s -> s.getUser().getId() == userId);

            entityManager.getTransaction().rollback();

        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
            beforeAll();

        }
    }


    @Test
    void deleteById_sessionExists_ReturnsTrue() {
        String sessionId = "session_1";
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            List<Session> sessions = (List<Session>) entityManager.createQuery("select s from Session s ").getResultList();

            assertThat(sessions.size()).isEqualTo(SESSIONS_COUNT);
            boolean deleted = sessionService.deleteById(sessionId);
            sessions = (List<Session>) entityManager.createQuery("select s from Session s ").getResultList();
            assertThat(deleted).isTrue();
            assertThat(sessions.size()).isEqualTo(SESSIONS_COUNT - 1);


            entityManager.getTransaction().rollback();
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
            beforeAll();
        }
    }

    @Test
    void deleteById_sessionNotExists_ReturnsFalse() {
        String sessionId = "session_-1";
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            List<Session> sessions = (List<Session>) entityManager.createQuery("select s from Session s ").getResultList();

            assertThat(sessions.size()).isEqualTo(SESSIONS_COUNT);
            boolean deleted = sessionService.deleteById(sessionId);
            sessions = (List<Session>) entityManager.createQuery("select s from Session s ").getResultList();
            assertThat(deleted).isFalse();
            assertThat(sessions.size()).isEqualTo(SESSIONS_COUNT);


            entityManager.getTransaction().rollback();
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
            beforeAll();
        }
    }

}
