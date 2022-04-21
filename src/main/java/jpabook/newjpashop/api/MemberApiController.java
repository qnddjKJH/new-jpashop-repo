package jpabook.newjpashop.api;

import jpabook.newjpashop.domain.Member;
import jpabook.newjpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // 가장 안 좋은 버전
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        // 엔티티를 직접적으로 반환한다? 진짜진짜진짜 추천하지 않는다.
        // 기본적으로 모든 값이 외부로 노출된다.
        // 회원 정보를 원하는데 엔티티를 주면 회원기준 주문 정보또한 넘어간다.
        // 원하지 않는 정보 같은 경우 @JsonIgnore 어노테이션을 해당 필드에 넣어줘도 되지만 --> 프레젠테이션 로직이 엔티티에 추가되기 시작함
        // 주문 정보도 필요한 다른 API 라면??...심각해진다.
        // 엔티티를 변경하게 되는 경우 API 스펙이 변경됨으로 API 를 사용하는 측에서는?? 매우 곤란해진다.
        // 몇번이고 강조하신다. 엔티티는 순정 그대로를 유지한다.
        // 아예 강제시키신다. 하지마라 그냥
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> members = memberService.findMembers();
        List<MemberDto> collect = members.stream()
                .map(m -> new MemberDto(m.getUsername()))
                .collect(Collectors.toList());
        // 이렇게 다른 데이터도 넣기 괜찮아 진다.
        return new Result(collect.size(), collect);
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        // @RequestBody 는 JSON 으로 온 body 를 Member 로 매핑 해준다.
        // 엔티티를 외부에서 JSON 으로 바인딩해서 사용해버리면 큰 장애가 너무 많이 발생한다.
        // 화면에서 들어오는 검증 로직이 엔티티에 들어가게 되면 너무 꼬인다
        // 여기는 NotEmpty 가 필요한데 다른곳은 필요하지 않을 경우 난처해 진다.
        // Entity 가 바껴버리면...API 스펙 자체가 바껴버리는 아주 큰 문제가 생긴다.
        // 그러니 파라미터로 받지도 말고 외부에 노출시키지도 말자. -> v2 로 해결
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * v1 을 개선한 모습
     * API 스펙이 이제는 바뀔 수 없다.
     * 값이 바뀌면 컴파일 오류가 나기 때문에 API 에 영향이 가지 않는다.
     * @param request (String name)
     * @return CreateMemberResponse in id
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = Member.builder()
                .username(request.getName())
                .build();
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(id, findMember.getUsername());
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }
}
