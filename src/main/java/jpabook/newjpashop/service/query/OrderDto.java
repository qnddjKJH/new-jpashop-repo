package jpabook.newjpashop.service.query;

import jpabook.newjpashop.domain.Address;
import jpabook.newjpashop.domain.Order;
import jpabook.newjpashop.domain.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderDto {
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
}
