package jpabook.newjpashop.service;

import jpabook.newjpashop.domain.Delivery;
import jpabook.newjpashop.domain.Member;
import jpabook.newjpashop.domain.Order;
import jpabook.newjpashop.domain.OrderItem;
import jpabook.newjpashop.domain.item.Item;
import jpabook.newjpashop.repository.ItemRepository;
import jpabook.newjpashop.repository.MemberRepository;
import jpabook.newjpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */

    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findById(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = Delivery.builder()
                .address(member.getAddress())
                .build();

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);
        // 주문 저장
        orderRepository.save(order);
        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 조회하면서 영속성 컨텍스트 올라가서
        // 조회된 엔티티의 값이 변경되면 변경된 내역이 적용된다.
        // 더티 체킹(변경 감지)
        order.cancel();
    }

    /**
     * 검색
     */
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch);
//    }
}
