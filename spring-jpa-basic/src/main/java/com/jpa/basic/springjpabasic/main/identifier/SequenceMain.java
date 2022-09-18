package com.jpa.basic.springjpabasic.main.identifier;

import com.jpa.basic.springjpabasic.domain.ActivityLog;
import com.jpa.basic.springjpabasic.jpa.EMFOracle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SequenceMain {
    private static Logger log = LoggerFactory.getLogger(SequenceMain.class);

    public static void main(String[] args) {
        EMFOracle.init();
        EntityManager em = EMFOracle.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            ActivityLog activityLog = new ActivityLog("U01", "VISIT");

            log.info("persist 실행 전");
            em.persist(activityLog);
            log.info("persist 실행 후");

            log.info("생성한 식별자: {}", activityLog.getId());

            log.info("커밋 전");
            transaction.commit();
            log.info("커밋 완료");
        } catch (Exception ex) {
            transaction.rollback();
        } finally {
            EMFOracle.close();
        }
    }
}
