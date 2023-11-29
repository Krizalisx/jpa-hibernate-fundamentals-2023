package com.tutorial;

import java.util.List;
import java.util.Map;

import org.hibernate.jpa.HibernatePersistenceProvider;

import com.tutorial.entities.Group;
import com.tutorial.entities.Passport;
import com.tutorial.entities.Person;
import com.tutorial.entities.User;
import com.tutorial.persistence.CustomPersistenceUnitInfo;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    private static EntityManager em = new HibernatePersistenceProvider()
        .createContainerEntityManagerFactory(new CustomPersistenceUnitInfo(), Map.of(
            "hibernate.hbm2ddl.auto", "update"
        ))
        .createEntityManager();

    public static void main(String[] args) {
        try {
            inTransaction(() -> {
                User user1 = new User();
                user1.setName("User 1");

                User user2 = new User();
                user2.setName("User 2");

                Group g1 = new Group();
                g1.setGroupName("Group 1");

                Group g2 = new Group();
                g2.setGroupName("Group 2");

                g1.setUsers(List.of(user1, user2));
                g2.setUsers(List.of(user2));

                user1.setGroups(List.of(g1));
                user2.setGroups(List.of(g1, g2));

                em.persist(user1);
                em.persist(user2);
                em.persist(g1);
                em.persist(g2);
            });
        } finally {
            em.close();
        }
    }

    private static void inTransaction(Runnable action) {
        em.getTransaction().begin();
        action.run();
        em.getTransaction().commit();
    }
}
