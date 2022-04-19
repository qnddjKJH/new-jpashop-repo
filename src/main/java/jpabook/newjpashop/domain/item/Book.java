package jpabook.newjpashop.domain.item;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("book")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Book extends Item {
    private String author;
    private String isbn;

    /**
     * 비즈니스 로직
     */

    // Book Update
    public void changeBookInfo(String name, int price, String author, String isbn) {
        this.changeItemInfo(name, price);
        this.author = author;
        this.isbn = isbn;
    }
}
