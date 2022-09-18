package com.jpa.basic.springjpabasic.main.identifier;


import com.jpa.basic.springjpabasic.domain.Review;
import com.jpa.basic.springjpabasic.jpa.EMF;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NativeMain {
    private static Logger log = LoggerFactory.getLogger(NativeMain.class);

    public static void main(String[] args) {
        EMF.init();
        EntityManager em = EMF.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Review review = new Review(5, "H-01", "작성자", "댓글");

            log.info("persist 실행 전");
            em.persist(review);
            log.info("persist 실행 후");

            log.info("생성한 식별자: {}", review.getId());

            log.info("커밋 전");
            transaction.commit();
            log.info("커밋 완료");
        } catch (Exception ex) {
            transaction.rollback();
        } finally {
            EMF.close();
        }
    }
}
