package jpabook.newjpashop.domain.item;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("movie")
@Getter
public class Movie extends Item {

    private String director;
    private String actor;

}
