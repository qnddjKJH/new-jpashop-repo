package jpabook.newjpashop.api;

import jpabook.newjpashop.domain.Address;
import jpabook.newjpashop.domain.Order;
import jpabook.newjpashop.domain.OrderItem;
import jpabook.newjpashop.domain.OrderStatus;
import jpabook.newjpashop.repository.OrderRepository;
import jpabook.newjpashop.repository.OrderSearch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getUsername();
            order.getDelivery().getAddress();

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        // 아니나 다를까 쿼리가 끔찍하게도 어마어마하게 나간다.
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();

        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return result;
    }

    @Getter
    static class OrderItemDto {
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }

    @Getter
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;
//        private List<OrderItem> orderItems;

        // Entity 와 연결을 완전히 끊은 예
        // 주석처리된 생성자는 존재하면 안된다.
        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getUsername();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(OrderItemDto::new)
                    .collect(Collectors.toList());
        }
/*
        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getUsername();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems();
            // null 이 뜨는데 왜?
            // Entity 니깐 Lazy 초기화 하지 않았으니깐 null 이 뜬다
            // v1 처럼 초기화 해주면 뜨긴 하지만 Entity 노출 하면 안된다
            // DTO 안에서도 Entity 를 넣어서 준다고 끝이 아니다
            // Entity 랑은 완전히 연결을 끊어야 한다.
        }
*/
    }
}
