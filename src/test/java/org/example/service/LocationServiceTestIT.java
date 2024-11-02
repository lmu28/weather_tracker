package org.example.service;

import org.example.model.Location;
import org.example.model.User;
import org.example.repository.LocationRepositoryHibernate;
import org.example.repository.UserRepositoryHibernate;
import org.example.service.dto.LocationDTO;
import org.example.util.CryptUtil;
import org.example.util.HibernateUtil;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

class LocationServiceTestIT {


    public static final long USER_ID = 1L;
    public static final int LOCATIONS_IN_LIST_COUNT = 2;
    public static final int LOCATIONS_COUNT = 8;
    public static final String LOCATION_NAME = "New York";
    private UserService userService = new UserService(new UserRepositoryHibernate(), new CryptUtil());

    private OpenWeatherService openWeatherService = new OpenWeatherService();


    private LocationService locationService = new LocationService(openWeatherService, userService, new LocationRepositoryHibernate());

    private static User user;
    private static Location unavailableLocation = new Location("nowNotExistingLocation", 0, 0);


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
    void getCurrentTemps() {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();

            user = entityManager.find(User.class, USER_ID);
            user.getLocations().add(unavailableLocation);
            entityManager.persist(unavailableLocation);
            entityManager.merge(user);
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }

        List<LocationDTO> locationDTOList = locationService.getCurrentTemps(user);
        assertTrue(user.getLocations().contains(unavailableLocation));
        assertThat(locationDTOList.size()).isEqualTo(LOCATIONS_IN_LIST_COUNT);


        entityManager = HibernateUtil.getEntityManager();
        User userWithUpdatedLocations = null;
        try {
            entityManager.getTransaction().begin();


            userWithUpdatedLocations = entityManager.find(User.class, USER_ID);

            List<Object> updatedLocationsList = entityManager.
                    createQuery("select l from Location l ").getResultList();

            assertThat(updatedLocationsList.size()).isEqualTo(LOCATIONS_COUNT);

            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }

        assertFalse(userWithUpdatedLocations.getLocations().contains(unavailableLocation));
        beforeAll();
    }


    @Test
    void save() {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            List<Object> list = entityManager.createQuery("select l from Location l ").getResultList();
            assertThat(list.size()).isEqualTo(LOCATIONS_COUNT);
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }

        Location location = new Location("Moscow", 55.7, 37.6);

        locationService.save(location);

        entityManager = HibernateUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            List<Object> list = entityManager.createQuery("select l from Location l ").getResultList();
            assertThat(list.size()).isEqualTo(LOCATIONS_COUNT + 1);

            Location savedlocation = locationService.findByName(location.getName());

            assertThat(savedlocation).matches(saved -> saved.compareTo(location) == 0);

            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
            beforeAll();
        }


    }






    @Test
    void findByName_locationExists_returnsLocation() {
        Location location = locationService.findByName(LOCATION_NAME);
        assertThat(location).isNotNull();
        assertThat(location.getName()).isEqualTo(LOCATION_NAME);


    }

    @Test
    void findByName_locationNotExists_returnsNull() {
        String notRealName = "";
        Location location = locationService.findByName(notRealName);
        assertThat(location).isNull();

    }

    @Test
    void findByName_locationIsNull_returnsNull() {
        Location location = locationService.findByName(null);
        assertThat(location).isNull();

    }



}
