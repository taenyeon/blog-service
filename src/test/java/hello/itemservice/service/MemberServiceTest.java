package hello.itemservice.service;

import hello.itemservice.domain.member.Member;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import hello.itemservice.repository.member.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository memoryMemberRepository;

    @BeforeEach
    public void beforeEach(){
        memoryMemberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memoryMemberRepository);
    }

    @AfterEach
    void afterEach(){
        memoryMemberRepository.clearStore();
    }


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
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }
    @Test
    void primaryJoin(){
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
        Member member1 = new Member("member", "member", "member");
        Member member2 = new Member("member", "member", "member");
    }

    /**
     * 회원 조회
     */
    @Test
    void findMember() {
    }
}