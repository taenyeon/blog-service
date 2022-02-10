package hello.blogService.repository;

import hello.blogService.dto.Member;
import hello.blogService.dto.OAuthUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberRepository {

    Optional<OAuthUser> findByEmail(String email);

    void saveOAuthUser(OAuthUser oAuthUser);
}
