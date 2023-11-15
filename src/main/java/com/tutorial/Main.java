package com.tutorial;

import java.util.HashMap;

import org.hibernate.jpa.HibernatePersistenceProvider;

import com.tutorial.entities.Employee;
import com.tutorial.entities.Product;
import com.tutorial.persistence.CustomPersistenceUnitInfo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        EntityManagerFactory emf = new HibernatePersistenceProvider().createContainerEntityManagerFactory(new CustomPersistenceUnitInfo(), new HashMap());
        EntityManager em = emf.createEntityManager(); // represents the context

        try {
            em.getTransaction().begin();

            Employee employee2 = new Employee();
            employee2.setId(5L);
            employee2.setName("Some5");
            employee2.setAddress("Another5");

            em.merge(employee2);


            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
