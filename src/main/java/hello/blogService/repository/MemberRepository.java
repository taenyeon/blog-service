package hello.blogService.repository;

import hello.blogService.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberRepository {
    int save(Member member);
    Optional<Member> findById(@Param("memberId") String memberId);
    List<Member> findAll();
    Optional<Member> login(@Param("memberId") String memberId,@Param("memberPwd") String memberPwd);
    int update(Member member);
    Optional<Member> findByEmail(@Param("memberEmail") String memberEmail);
}
