package com.tutorial;

import com.tutorial.entities.jpql.joins_and_inner_queries.Participant;
import com.tutorial.persistence.CustomPersistenceUnitInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Map;
import org.hibernate.jpa.HibernatePersistenceProvider;

public class Main {

    private static EntityManager em = new HibernatePersistenceProvider()
        .createContainerEntityManagerFactory(new CustomPersistenceUnitInfo(), Map.of(
            "hibernate.hbm2ddl.auto", "update"
        ))
        .createEntityManager();

    public static void main(String[] args) {
        try {
            inTransaction(() -> {
                // named get all query
                TypedQuery<Participant> getAll = em.createNamedQuery("getAll", Participant.class);
                getAll.getResultList().forEach(System.out::println);

                System.out.println();

                // named get by name query
                TypedQuery<Participant> getByName = em.createNamedQuery("getByName", Participant.class);
                getByName.setParameter("name", "Alice");
                getByName.getResultList().forEach(System.out::println);

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
