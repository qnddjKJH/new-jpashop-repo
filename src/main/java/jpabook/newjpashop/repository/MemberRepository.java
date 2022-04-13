package jpabook.newjpashop.repository;

import jpabook.newjpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;
    // 스프링 부트를 사용하기에 @PersistenceContext 지정하면
    // EntityManagerFactory 에서 EntityManager 를 가져오는 코드를 쓰지 않아도 된다.
    // 왜? 스프링 컨테이너 위에서 동작해서 스프링 부트가 다 해줌

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member findById(Long findId) {
        return em.find(Member.class, findId);
    }

    public List<Member> findByName(String username) {
        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
