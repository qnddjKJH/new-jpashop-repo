package jpabook.newjpashop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

//    @NotEmpty 이 검증 로직은 이제 API 쪽 DTO 에서 사용한다. 깔-끔
    private String username;

    @Embedded
    private Address address;

    @Builder.Default
    @OneToMany(mappedBy = "member") // Order Class 의 member 필드
    private List<Order> orders = new ArrayList<>();

    /**
     * 비즈니스 로직
     */
    public void changeName(String username) {
        this.username = username;
    }
}
