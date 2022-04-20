package jpabook.newjpashop.service;

import jpabook.newjpashop.domain.Member;
import jpabook.newjpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     *  회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);

        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findById(id);
        member.changeName(name);
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    private void validateDuplicateMember(Member member) {
        // EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getUsername());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        // 이렇게 해도 동시성 문제가 있을 수 있으니
        // 최후의 방어로 db 에서 username 에  유니크 제약 조건을 걸어두는 것이 좋다.
    }
}
