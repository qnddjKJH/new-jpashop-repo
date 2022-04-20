package jpabook.newjpashop.api;

import jpabook.newjpashop.domain.Member;
import jpabook.newjpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

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
}
