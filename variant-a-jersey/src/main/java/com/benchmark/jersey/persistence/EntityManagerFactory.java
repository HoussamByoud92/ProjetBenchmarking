package com.benchmark.jersey.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class EntityManagerFactory {
    private static final jakarta.persistence.EntityManagerFactory emf = 
        Persistence.createEntityManagerFactory("benchmark-jersey");
    
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public static void close() {
        emf.close();
    }
}
