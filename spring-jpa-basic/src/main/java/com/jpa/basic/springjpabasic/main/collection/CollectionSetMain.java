package com.jpa.basic.springjpabasic.main.collection;

import com.jpa.basic.springjpabasic.domain.GrantedPermission;
import com.jpa.basic.springjpabasic.domain.Role;
import com.jpa.basic.springjpabasic.jpa.EMF;
import com.jpa.basic.springjpabasic.main.identifier.NativeMain;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class CollectionSetMain {
    private static Logger log = LoggerFactory.getLogger(NativeMain.class);
    private static EntityManager em;
    private static EntityTransaction transaction;

    static {
        EMF.init();
        em = EMF.createEntityManager();
        transaction = em.getTransaction();
    }

    public static void main(String[] args) {
        try {
//            save(new Role("id-3", "관리자", Set.of(
//                    new GrantedPermission("perm-3", "g-1"),
//                    new GrantedPermission("perm-4", "g-2"))));

            findById("id-3");
        } catch (Exception ex) {
            log.error("Exception = {}", ex.getMessage());
            transaction.rollback();
        } finally {
            EMF.close();
        }
    }

    private static void save(Role role) {
        transaction.begin();

        log.info("role = {}", role);
        em.persist(role);

        transaction.commit();
    }

    private static void findById(String roleId) {
transaction.begin();

log.info("find 실행 전");
Role role = em.find(Role.class, roleId);
log.info("find 실행 후");

for (GrantedPermission perm : role.getPermissions()) {
    log.info("perm : {}", perm);
}

transaction.commit();
    }
}
