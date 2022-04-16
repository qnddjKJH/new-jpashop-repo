package jpabook.newjpashop.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(value = EnumType.STRING)    // ORDINAL 절대 사용 금지
    private DeliveryStatus status;

    // 연관관계 메서드
    public void setOrder(Order order) {
        this.order = order;
    }
}
