package com.meta.jpa_core;

import com.meta.jpa_core.entity.Memo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EntityTest {

    EntityManagerFactory emf;
    EntityManager em;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("memo");
        em = emf.createEntityManager();
    }


    @Test
    @DisplayName("EntityTransaction 성공 테스트")
    void test1() {
        EntityTransaction et = em.getTransaction(); // EntityManager 에서 EntityTransaction 을 가져옵니다.

        et.begin(); // 트랜잭션을 시작합니다.

        try { // DB 작업을 수행합니다.

            Memo memo = new Memo(); // 저장할 Entity 객체를 생성합니다.
            memo.setId(1L); // 식별자 값을 넣어줍니다.
            memo.setUsername("Meta");
            memo.setContents("영속성 컨텍스트와 트랜잭션 이해하기");

            em.persist(memo); // EntityManager 사용하여 memo 객체를 영속성 컨텍스트에 저장합니다.

            et.commit(); // 오류가 발생하지 않고 정상적으로 수행되었다면 commit 을 호출합니다.
            // commit 이 호출되면서 DB 에 수행한 DB 작업들이 반영됩니다.
        } catch (Exception ex) {
            ex.printStackTrace();
            et.rollback(); // DB 작업 중 오류 발생 시 rollback 을 호출합니다.
        } finally {
            em.close(); // 사용한 EntityManager 를 종료합니다.
        }

        emf.close(); // 사용한 EntityManagerFactory 를 종료합니다.
    }

    @Test
    @DisplayName("EntityTransaction 실패 테스트")
    void test2() {
        EntityTransaction et = em.getTransaction(); // EntityManager 에서 EntityTransaction 을 가져옵니다.

        et.begin(); // 트랜잭션을 시작합니다.

        try { // DB 작업을 수행합니다.

            Memo memo = new Memo(); // 저장할 Entity 객체를 생성합니다.
            memo.setUsername("META");
            memo.setContents("실패 케이스");

            em.persist(memo); // EntityManager 사용하여 memo 객체를 영속성 컨텍스트에 저장합니다.

            et.commit(); // 오류가 발생하지 않고 정상적으로 수행되었다면 commit 을 호출합니다.
            // commit 이 호출되면서 DB 에 수행한 DB 작업들이 반영됩니다.
        } catch (Exception ex) {
            System.out.println("식별자 값(ID)을 넣어주지 않아 오류가 발생했습니다.");
            ex.printStackTrace();
            et.rollback(); // DB 작업 중 오류 발생 시 rollback 을 호출합니다. <<<<<<<<
        } finally {
            em.close(); // 사용한 EntityManager 를 종료합니다.
        }

        emf.close(); // 사용한 EntityManagerFactory 를 종료합니다.
    }

    @Test
    @DisplayName("Entity 조회 : 캐시 저장소에 해당하는 Id가 존재하지 않은 경우")
    void test3() {
        try {

            Memo memo = em.find(Memo.class, 1);
            System.out.println("memo.getId() = " + memo.getId());
            System.out.println("memo.getUsername() = " + memo.getUsername());
            System.out.println("memo.getContents() = " + memo.getContents());


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("Entity 조회 : 캐시 저장소에 해당하는 Id가 존재하는 경우")
    void test4() {
        try {

            Memo memo1 = em.find(Memo.class, 1);
            System.out.println("memo1 조회 후 캐시 저장소에 저장\n");

            Memo memo2 = em.find(Memo.class, 1);
            System.out.println("memo2.getId() = " + memo2.getId());
            System.out.println("memo2.getUsername() = " + memo2.getUsername());
            System.out.println("memo2.getContents() = " + memo2.getContents());


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("객체 동일성 보장")
    void test5() {
        EntityTransaction et = em.getTransaction();

        et.begin();

        try {
            Memo memo3 = new Memo();
            memo3.setId(2L);
            memo3.setUsername("Meta");
            memo3.setContents("객체 동일성 보장");
            em.persist(memo3);

            Memo memo1 = em.find(Memo.class, 1);
            Memo memo2 = em.find(Memo.class, 1);
            Memo memo  = em.find(Memo.class, 2);

            System.out.println(memo1 == memo2);
            System.out.println(memo1 == memo);

            et.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            et.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("Entity 삭제")
    void test6() {
        EntityTransaction et = em.getTransaction();

        et.begin();

        try {

            Memo memo = em.find(Memo.class, 2);

            em.remove(memo);

            et.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
            et.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}