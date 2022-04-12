package jpabook.newjpashop.domain.item;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("album")
@Getter
public class Album extends Item {
    private String artist;
    private String etc;
}
