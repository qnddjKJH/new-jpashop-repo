package jpabook.newjpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    // 한쪽을 안 막아두면 무한하게 서로를 부름
    // 양방향 관계라면 주인이 아닌 한쪽은 막아둬야 한다.
    @JsonIgnore 
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
