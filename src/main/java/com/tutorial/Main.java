package com.tutorial;

import java.util.Map;

import org.hibernate.jpa.HibernatePersistenceProvider;

import com.tutorial.entities.Passport;
import com.tutorial.entities.Person;
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
                Person person = new Person();
                person.setName("Laur");

                Passport passport = new Passport();
                passport.setNumber("abc123");
                passport.setPerson(person);

                person.setPassport(passport);

                em.persist(person);

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
