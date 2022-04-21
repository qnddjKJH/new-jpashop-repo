package jpabook.newjpashop;

import jpabook.newjpashop.domain.*;
import jpabook.newjpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * 총 주문 2개
 * - userA
 *     - JPA1 BOOK
 *     - JPA2 BOOK
 * - userB
 *     - SPRING1 BOOK
 *     - SPRING2 BOOK
 */

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        
        private final EntityManager em;

        public void dbInit1() {
            Member member = getMember("userA", "서울", "1", "111");
            em.persist(member);

            Book book1 = getBook("JPA1 BOOK", 10000, 100);
            em.persist(book1);

            Book book2 = getBook("JPA2 BOOK", 20000, 200);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = Delivery.builder()
                    .address(member.getAddress())
                    .build();
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = getMember("userB", "부산", "2", "222");
            em.persist(member);

            Book book1 = getBook("SPRING1", 30000, 200);
            em.persist(book1);

            Book book2 = getBook("SPRING2", 40000, 300);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

            Delivery delivery = Delivery.builder()
                    .address(member.getAddress())
                    .build();
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private Member getMember(String name, String city, String street, String zipcode) {
            Member member = Member.builder()
                    .username(name)
                    .address(new Address(city, street, zipcode))
                    .build();
            return member;
        }

        private Book getBook(String name, int price, int stockQuantity) {
            return Book.builder()
                    .name(name)
                    .price(price)
                    .stockQuantity(stockQuantity)
                    .build();
        }
    }
}
