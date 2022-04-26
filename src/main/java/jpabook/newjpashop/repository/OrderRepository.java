package jpabook.newjpashop.repository;

import jpabook.newjpashop.domain.Order;
import jpabook.newjpashop.repository.order.simplequery.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }

    // 검색을 하는데 조건 추가해서 리스트 서치 == 동적쿼리
    public List<Order> findAllByString(OrderSearch orderSearch) {
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    /**
     * JPA Criteria
     * 단점 - 유지보수가 너무 힘들다. : 실무에 적합하지 않음
     * 어떠한 쿼리가 날라갈지 예측 하기가 힘들다.
     *
     * 동적 쿼리, 정적 쿼리 복잡해진다고 느껴지면
     * QueryDsl 로 작업하는 것을 적극 추천한다.
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.get("name"), "%" + orderSearch.getMemberName());
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select distinct o from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d", Order.class
        ).getResultList();
    }

    public List<Order> findAllWithItem() {
        // distinct 기능
        // 1. distinct 를 쿼리를 붙여서 보낸다.
        // 2. jpa 에서 자체적으로 order 가 같은 id 값이면
        // == order 의 객체가 같다면 jpa 가 객체를 중복 제거 -> 애플리케이션쪽이다.
        // fetch join 으로 쿼리가 10번 넘게 나가던게 여기서 1번 나간다.
        return em.createQuery(
                "select distinct o from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d " +
                        "join fetch o.orderItems oi " +
                        "join fetch oi.item i", Order.class
        ).getResultList();
        // 치명적 단점 :: 일대다 관계를 fetch join 하면
        // 페이징이 불가능 -- 불가능하며 사용하지 말 것 다른 대안을 사용
        // 메모리에서 작업하겠다고 하이버네이트가 warn 메시지를 쏜다
        // 몇 건 안되면 상관은 없는데 1만건 100만건이 어플리케이션
        // 메모리에 퍼올려진다면?? 끔찍한 일이 발생한다.
        // 이건 join 의 특징때문 개수 뻥튀기로 갯수를 예측할 수 없기 때문이다.
        
        // + 컬렉션 페치 조인은 1개만 둘 이상에서 사용 하지 말것
        // 데이터 부정합이 일어날 수 있다.
    }

}
