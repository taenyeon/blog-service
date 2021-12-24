package hello.itemservice.service;

import hello.itemservice.domain.member.Member;
import hello.itemservice.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    /**
     * 회원가입
     */
    public String join(Member member) {
        // 중복 X
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findById(member.getId()).
                ifPresent(isMember -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 조회
     */
    public Optional<Member> findMember(String memberId) {
        return memberRepository.findById(memberId);
    }

    /**
     * 회원 로그인
     */
    public Optional<String> loginMember(Member member) {
        return memberRepository.login(member);
    }
}
