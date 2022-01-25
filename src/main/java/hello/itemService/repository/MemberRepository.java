package hello.itemService.repository;

import hello.itemService.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberRepository {
    int save(Member member);
    Optional<Member> findById(String id);
    List<Member> findAll();
    Optional<Member> login(@Param("id") String id,@Param("pwd") String pwd);
    int update(Member member);
    Optional<Member> findByEmail(@Param("email") String email);
}
