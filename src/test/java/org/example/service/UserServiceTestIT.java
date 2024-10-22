package org.example.service;

import org.example.model.Session;
import org.example.model.User;
import org.example.repository.UserRepositoryHibernate;
import org.example.util.CryptUtil;
import org.example.util.HibernateUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTestIT {


    public static final int COUNT_OF_USERS = 5;
    public  UserService userService = new UserService(new UserRepositoryHibernate(), new CryptUtil());

    @BeforeAll
    static void beforeAll() {
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
    void findByName_Exists_ReturnsValidUser() {
        String name  = "Alice";
        User user = userService.findByName(name);
        assertThat(user).isNotNull();
    }

    @Test
    void findByName_Not_Exists_ReturnsNull() {
        String name  = "100qwe";
        User user = userService.findByName(name);
        assertThat(user).isNull();
    }

    @Test
    void save(){

        User userToSave = new User(0,"John","1234j5678", null);

        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            List<User> users =(List<User>) entityManager.createQuery("select u from User u ").getResultList();

            assertThat(users.size()).isEqualTo(COUNT_OF_USERS);
            User user = userService.save(userToSave);

            users = (List<User>) entityManager.createQuery("select u from User u ").getResultList();

            assertThat(users.size()).isEqualTo(COUNT_OF_USERS+1);

            assertThat(user)
                    .matches(u -> u.getId() != 0);
            entityManager.getTransaction().rollback();

        }catch (RuntimeException e){
            entityManager.getTransaction().rollback();
        }finally {
            entityManager.close();
        }
    }
}