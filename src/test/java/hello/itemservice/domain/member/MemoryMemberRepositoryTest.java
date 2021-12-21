package hello.itemservice.domain.member;

import hello.itemservice.repository.member.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

public class MemoryMemberRepositoryTest {
    MemoryMemberRepository memoryMemberRepository = new MemoryMemberRepository();

    @AfterEach
    void afterEach() {
        memoryMemberRepository.clearStore();
    }

    @Test
    void save(){
        // given
        Member member = new Member();
        member.setId("i123");
        member.setPwd("dfshkjds");
        member.setName("홍길동");
        //when
        Optional<Member> savedMember = Optional.ofNullable(memoryMemberRepository.save(member));
        //then
        Optional<Member> findMember = memoryMemberRepository.findById(member.getId());
        assertThat(findMember).isEqualTo(savedMember);

    }

    @Test
    void findAll(){
        //given
        Member member1 = new Member();
        member1.setId("i123");
        member1.setPwd("dfshkjds");
        member1.setName("홍길동");
        Member member2 = new Member();
        member2.setId("i1234");
        member2.setPwd("dfshkjdsfds");
        member2.setName("홍길순");
        memoryMemberRepository.save(member1);
        memoryMemberRepository.save(member2);

        //when
        List<Member> result = memoryMemberRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(member1,member2);

    }
}
