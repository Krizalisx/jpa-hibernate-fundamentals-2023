package com.tutorial;

import java.util.Map;

import org.hibernate.jpa.HibernatePersistenceProvider;

import com.tutorial.entities.inheritance.Book;
import com.tutorial.entities.inheritance.ElectronicDevice;
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
                Book book = new Book();
                book.setAuthor("Author");

                ElectronicDevice electronicDevice = new ElectronicDevice();
                electronicDevice.setVoltage(220);

                em.persist(book);
                em.persist(electronicDevice);


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
