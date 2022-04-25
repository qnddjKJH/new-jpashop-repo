package jpabook.newjpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    /**
     * Repository 는 순수한 엔티티를 조회하는 용도로 만들기 위해
     * DTO 를 조회하는 쿼리에 관한거는 따로 분리하는 것을 추천한다.
     */

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                "select new jpabook.newjpashop.repository.order.simplequery." +
                        "OrderSimpleQueryDto(o.id, m.username, o.orderDate, o.status, d.address) " +
                        "from Order o " +
                        "join o.member m " +
                        "join o.delivery d", OrderSimpleQueryDto.class
        ).getResultList();
    }
}
