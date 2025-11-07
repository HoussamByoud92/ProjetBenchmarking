package com.benchmark.jersey.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaConfig {
    private static EntityManagerFactory emf;
    private static final Object lock = new Object();

    public static EntityManager getEntityManager() {
        if (emf == null) {
            synchronized (lock) {
                if (emf == null) {
                    emf = Persistence.createEntityManagerFactory("benchmark-pu");
                }
            }
        }
        return emf.createEntityManager();
    }

    public static void closeEMF() {
        if (emf != null) {
            emf.close();
        }
    }
}
