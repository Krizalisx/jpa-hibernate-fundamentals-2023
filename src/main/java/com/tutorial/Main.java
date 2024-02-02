package com.tutorial;

import com.tutorial.entities.jpql.joins_and_inner_queries.Enrollment;
import com.tutorial.entities.jpql.joins_and_inner_queries.Participant;
import com.tutorial.persistence.CustomPersistenceUnitInfo;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;
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
                EntityGraph<Participant> participantGraph = em.createEntityGraph(Participant.class);
                participantGraph.addAttributeNodes("enrollments");

                em.createQuery("select p from Participant p", Participant.class)
//                    .setHint("jakarta.persistence.fetchgraph", em.getEntityGraph("Participant.enrollments"))
                    .setHint("jakarta.persistence.fetchgraph", participantGraph)
                    .getResultList()
                    .stream()
                    .map(Participant::getEnrollments)
                    .flatMap(List::stream)
                    .forEach(e -> {});
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
