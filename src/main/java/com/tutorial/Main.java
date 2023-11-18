package com.tutorial;

import java.util.Map;

import org.hibernate.jpa.HibernatePersistenceProvider;

import com.tutorial.entities.Product;
import com.tutorial.entities.Student;
import com.tutorial.entities.keys.StudentKey;
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
                Product product = new Product();
                product.setCode("ABC");
                product.setNumber(10L);
                product.setColor("Red");

                em.persist(product);

                StudentKey studentKey = new StudentKey();
                studentKey.setCode("ABC");
                studentKey.setNumber(10L);

                Student student = new Student();
                student.setId(studentKey);
                student.setName("John");

                em.persist(student);
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
