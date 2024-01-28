package com.tutorial;

import com.tutorial.entities.jpql.Instrument;
import com.tutorial.entities.jpql.joins_and_inner_queries.dto.EnrolledStudent;
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
                // simple query
                String simpleQuery = """
                    select lhs from Instrument lhs
                    left join Instrument rhs on lhs.id <= rhs.id
                    """;
                TypedQuery<Instrument> query = em.createQuery(simpleQuery, Instrument.class);
                System.out.println(query.getResultList());

                // jpql join with projection
                String jpql2 = """
                    select new com.tutorial.entities.jpql.joins_and_inner_queries.dto.EnrolledStudent(p, e) from Participant p inner join p.enrollments e
                    """;
                TypedQuery<EnrolledStudent> query2 = em.createQuery(jpql2, EnrolledStudent.class);
                query2.getResultList().forEach(enrolledStudent -> {
                    System.out.println(enrolledStudent.participant() + " " + enrolledStudent.enrollment());
                });

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
