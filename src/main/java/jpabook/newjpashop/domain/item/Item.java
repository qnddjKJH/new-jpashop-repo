package jpabook.newjpashop.domain.item;

import jpabook.newjpashop.domain.Category;
import jpabook.newjpashop.exception.NotEnoughStockException;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@SuperBuilder // lombok 1.18.2 버전 이상부터 상속 Builder 구현 가능한 어노테이션
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @Builder.Default
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // 비즈니스 로직 //

    /**
     * 아이템 변경
     */
    public void changeItemInfo(String name, int price) {
        this.name = name;
        this.price = price;
    }

    /**
     * 재고 증가
     */
    public void addStockQuantity(int stockQuantity) {
        this.stockQuantity += stockQuantity;
    }

    /**
     * 재고 감소
     */
    public void removeStock(int stockQuantity) throws NotEnoughStockException {
        int restStock = this.stockQuantity - stockQuantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
