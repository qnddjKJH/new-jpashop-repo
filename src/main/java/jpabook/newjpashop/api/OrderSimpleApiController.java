package jpabook.newjpashop.api;


import jpabook.newjpashop.domain.Order;
import jpabook.newjpashop.repository.OrderRepository;
import jpabook.newjpashop.repository.OrderSearch;
import jpabook.newjpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.newjpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * XToOne(ManyToOne, OneToOne) 관계에서의 성능 최적화
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        return orders;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<OrderSimpleQueryDto> ordersV2() {
        // 절대적으로 dto 로 바꿔서 보내길 바란다.
        // 이제 문제는 lazy loading 으로 인한 엄청난 양의 쿼리가 날라가는 문제
        return orderRepository.findAllByString(new OrderSearch()).stream()
                .map(OrderSimpleQueryDto::new)
                .collect(Collectors.toList());

        /*
        * Order 2개 조회 { findAllByString }
        * SimpleOrderDto 생성자에서 getMember() 에서 Member 조회하는 쿼리 1
        * 마찬가지로 Delivery 조회 시 1개
        * Order 는 2개 이므로 2번의 루프를 돈다.
        * 1 + 2 + 2 = 5번 <- 최악의 경우
        * 1 + N 문제 (N+1 문제)
        */
    }

    @GetMapping("/api/v3/simple-orders")
    public List<OrderSimpleQueryDto> orderV3() {
        // 쿼리 단 하나 너무 좋다!
        return orderRepository.findAllWithMemberDelivery()
                .stream()
                .map(OrderSimpleQueryDto::new)
                .collect(Collectors.toList());
    }

    // v3 와 v4 는 우열을 가리기는 힘들다.
    // 단지 v3 는 재사용성이 높고
    // v4 는 재사용성이 낮다
    // 하지만
    // v4 는 엔티티의 접근을 아예 막을 수 있고
    // 성능도 select 절에서 데이터 절감을 노릴 수 있다. (네트워크의 발전으로 장점인지는...)
    // 데이터 사이즈가
    // 단점은 코드가 지전분해질 수 있다.
    // repository 에 api 스펙이 들어오는 느낌이라 api 스펙이 변경되면
    // 코드를 api 스펙에 맞춰서 바꿔야한다.
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }
}
