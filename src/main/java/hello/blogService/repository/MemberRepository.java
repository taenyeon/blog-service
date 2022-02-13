package hello.blogService.repository;

import hello.blogService.dto.OAuthUser;
import org.apache.ibatis.annotations.Mapper;
import java.util.Optional;

@Mapper
public interface MemberRepository {

    Optional<OAuthUser> findByEmail(String memberEmail);

    void saveOAuthUser(OAuthUser oAuthUser);
}
