package com.tutorial;

import com.tutorial.entities.jpql.joins_and_inner_queries.Enrollment;
import com.tutorial.entities.jpql.joins_and_inner_queries.Participant;
import com.tutorial.persistence.CustomPersistenceUnitInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.util.Arrays;
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

                CriteriaBuilder builder = em.getCriteriaBuilder();

                // select p from Participant p
                CriteriaQuery<Participant> query = builder.createQuery(Participant.class);
                Root<Participant> participantRoot = query.from(Participant.class);
                CriteriaQuery<Participant> cq = query.select(participantRoot);
                em.createQuery(cq).getResultList().forEach(System.out::println);

                System.out.println();

                // select p.name from Participant p
                CriteriaQuery<String> query1 = builder.createQuery(String.class);
                Root<Participant> stringRoot1 = query1.from(Participant.class);
                CriteriaQuery<String> cq1 = query1.select(stringRoot1.get("name"));
                em.createQuery(cq1).getResultList().forEach(System.out::println);

                System.out.println();

                // select p.name, p.id from Participant p
                CriteriaQuery<Object[]> query2 = builder.createQuery(Object[].class);
                Root<Participant> stringRoot2 = query2.from(Participant.class);
                CriteriaQuery<Object[]> cq2 = query2.multiselect(stringRoot2.get("name"), stringRoot2.get("id"), builder.count(stringRoot2.get("id")))
                    .orderBy(builder.asc(stringRoot2.get("name")))
                    .groupBy(stringRoot2.get("name"))
                    .having(builder.gt(builder.count(stringRoot2.get("id")), 1));
                em.createQuery(cq2).getResultList().stream().map(Arrays::toString).forEach(System.out::println);

                System.out.println();

                // select p from Participant p join p.enrollments e
                CriteriaQuery<Tuple> tupleQuery = builder.createTupleQuery();
                Root<Participant> participantRoot1 = tupleQuery.from(Participant.class);
                Join<Participant, Enrollment> joinParticipant = participantRoot1.join("enrollments", JoinType.LEFT);
                tupleQuery.multiselect(participantRoot1, joinParticipant); // select p, e from Participant p left join p.enrollments e
                em.createQuery(tupleQuery).getResultList().forEach(t -> System.out.println(t.get(0) + " " + t.get(1)));
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
