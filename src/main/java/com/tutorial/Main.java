package com.tutorial;

import com.tutorial.entities.jpql.Instrument;
import com.tutorial.persistence.CustomPersistenceUnitInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jpa.HibernatePersistenceProvider;

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
                // simple query
                String simpleQuery = """
                    select lhs from Instrument lhs
                    left join Instrument rhs on lhs.id <= rhs.id
                    """;
                TypedQuery<Instrument> query = em.createQuery(simpleQuery, Instrument.class);
                System.out.println(query.getResultList());

                // parameterized query
                String queryWithParams = """
                    select instrument from Instrument instrument
                    where name like :name
                    """;
                TypedQuery<Instrument> parameterizedQuery = em.createQuery(queryWithParams, Instrument.class);
                parameterizedQuery.setParameter("name", "%i%");
                System.out.println(parameterizedQuery.getResultList());

                // average
                String averageQuery = """
                    select avg(price) from Instrument
                    """;
                System.out.println(em.createQuery(averageQuery, BigDecimal.class).getSingleResult());

                // select particular columns
                String queryForParticularColumns = "select name, price from Instrument instrument";
                TypedQuery<Object[]> queryForParticularColumnsTyped = em.createQuery(queryForParticularColumns, Object[].class);
                queryForParticularColumnsTyped.getResultList().forEach(row -> System.out.println(row[0] + " " + row[1]));

                // get single result throws exception in case of no result
                String queryForSingleResult = "select instrument from Instrument instrument where name = 'unknown'";
                try {
                    em.createQuery(queryForSingleResult, Instrument.class).getSingleResult();
                } catch (NoResultException e) {
                    System.out.println("No result found!\n" + e);
                }

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
