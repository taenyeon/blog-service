package hello.itemservice.service;

import hello.itemservice.domain.member.Member;
import hello.itemservice.repository.member.MemberRepository;
import hello.itemservice.repository.member.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
// @Transactional 어노테이션을 추가하면
// 테스트에서 사용한 쿼리의 결과를 모두 롤백시켜주기 때문에
// 테스트 후에 그 값을 일일이 지우는 작업을 할 필요가 없다.
@Transactional
// 테스트에도 아래와 같이 spring을 올려서 테스트를 할 수 있지만,
// 이렇게 할 경우, 테스트 시간이 길어지고 코드가 복잡해지는 경우가 많다.
// 테스트시에는, 순수한 자바 코드로 테스트를 진행하는것이 좋은 테스트 방법이다.
// 이 방법으로 테스트가 불가능할 경우에는 그 테스트 환경의 문제나 코드에 대한 이해부족일 확률이 높다.
class MemberServiceIntegrationTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Test
    void join() {
        // given
        Member member = new Member("member", "member", "member");

        // when
        String joinMember = memberService.join(member);

        // then
        Member findMember = memberService.findMember(joinMember).get();
        assertThat(member.getId()).isEqualTo(findMember.getId());
    }

    @Test
    void primaryJoin() {
        // given
        Member member1 = new Member("member", "member", "member");
        Member member2 = new Member("member", "member", "member");
        // when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

        // then
    }

    /**
     * 전체 회원 조회
     */
    @Test
    void findMembers() {
        // given
        Member member1 = new Member("member1", "member1", "member1");
        Member member2 = new Member("member2", "member2", "member2");
        memberService.join(member1);
        memberService.join(member2);
        // when
        List<Member> members = memberService.findMembers();
        assertThat(members.get(0).getId()).isEqualTo("member1");
        assertThat(members.get(1).getId()).isEqualTo("member2");
    }

    /**
     * 회원 조회
     */
    @Test
    void findMember() {
    }
}