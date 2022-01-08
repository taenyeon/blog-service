package hello.itemService.service;

import hello.itemService.domain.Files;
import hello.itemService.domain.Member;
import hello.itemService.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    @Value("${spring.servlet.multipart.location}")
    String path;
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
        if (memberRepository.save(member) != 0) {
            return member.getId();
        } else {
            throw new IllegalStateException("DB 오류로 회원 가입에 실패하였습니다.");
        }
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
    public Optional<Member> loginMember(Member member) {
        Optional<Member> loginMember = memberRepository.login(member.getId(), member.getPwd());
        if (loginMember.isPresent()) {
            return loginMember;
        } else {
            throw new IllegalStateException("아이디 또는 비밀번호 오류입니다.");
        }
    }

    public String updateMember(Member member, MultipartFile file) throws IOException {
            String changedName = member.getId()+".jpg";
        if (!file.isEmpty()) {
            File f = new File(path + "/member/" + changedName);
            file.transferTo(f);
            member.setImg(changedName);
            System.out.println("OK");
        }
        int result = memberRepository.update(member);
        if (result != 0) {
            return member.getImg();
        } else {
            throw new IllegalStateException("업데이트에 실패하였습니다.");
        }
    }

    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
