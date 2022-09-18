package com.jpa.basic.springjpabasic.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EMFOracle {
    private static EntityManagerFactory emf;

    private EMFOracle() {
    }

    public static void init() {
        emf = Persistence.createEntityManagerFactory("oracle");
    }

    public static EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        emf.close();
    }
}
