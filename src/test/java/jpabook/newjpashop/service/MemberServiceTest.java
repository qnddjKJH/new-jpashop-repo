package jpabook.newjpashop.service;

import jpabook.newjpashop.domain.Member;
import jpabook.newjpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() {
        // when
        Member member = Member.builder()
                .username("test")
                .build();

        // given
        Long join = memberService.join(member);

        // then
        assertThat(join).isEqualTo(memberRepository.findById(join).getId());
    }

    @Test // (expected = IllegalStateException.class) - Junit4
    public void 회원가입_중복_예외() {
        // when
        Member member = Member.builder()
                .username("test")
                .build();

        Member member2 = Member.builder()
                .username("test")
                .build();

        // given
        memberService.join(member);
//        try {
//            memberService.join(member2);
//        } catch (IllegalStateException e) {
//            return;
//        }
//        memberService.join(member2);

        // then
//        fail("예외 발생");  - 주석 처리 == Junit4 방식
      
        // Junit5 는 assertThrow 를 사용 
        // Junit5 기반 assertj 는 다음과 같다
        assertThatThrownBy(() -> memberService.join(member2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 회원입니다.");
    }
}