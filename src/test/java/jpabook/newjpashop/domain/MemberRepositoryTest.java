package jpabook.newjpashop.domain;

import jpabook.newjpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    
    @Test
    @Transactional
    public void 멤버_레포지토리_테스트() {
        // when
        Member member = Member.builder()
                .username("테스트")
                .build();

        // given
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.findById(saveId);

        // then
        assertThat(saveId).isEqualTo(findMember.getId());
    }
}