package jpabook.newjpashop.repository;

import jpabook.newjpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // JpaRepository
    // 개발자들이 공통적으로 사용할 수 있는 save, findAll 등
    // 공통적으로 사용하는 메소드들이 이미 다 구현되어 있다

    List<Member> findByUsername(String username);
}

/**
 *  단순 편리함을 넘어서 마법같은 놀라운 개발 생산성을 제공해준다
 *  진짜 너무 환상적인데 만능은 아니다. - 경험담
 *  Spring Data JPA 는 JPA 를 사용해서 이런 기능을 제공할 뿐
 *  결국 JPA 자체를 잘 이해해야 한다. 
 *  
 *  진짜 돌고돌아 4년만에 왔다.
 *  Spring Data JPA 원리를 모르고
 *  사용만 하던 그때에서
 */
