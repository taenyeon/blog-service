package hello.blogService.service;

import hello.blogService.dto.OAuthAttributes;
import hello.blogService.dto.OAuthUser;
import hello.blogService.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
public class MyOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final HttpSession session;
    private final MemberRepository memberRepository;

    public MyOAuth2UserService(HttpSession session, MemberRepository memberRepository) {
        this.session = session;
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(
                registrationId, userNameAttributeName, oAuth2User.getAttributes());

        OAuthUser user = saveOrUpdate(attributes);
        session.setAttribute("user", user);
        DefaultOAuth2User defaultOAuth2User = new DefaultOAuth2User(Collections.singleton(
                new SimpleGrantedAuthority(user.getMemberRole()))
                , attributes.getAttributes()
                , attributes.getNameAttributeKey());
        log.info(defaultOAuth2User.toString());
        return defaultOAuth2User;
    }

    private OAuthUser saveOrUpdate(OAuthAttributes attributes) {
        Optional<OAuthUser> user = memberRepository.findByEmail(attributes.getEmail());
        if (user.isPresent()) {
            user.get().update(attributes.getName(), attributes.getPicture());
        } else {
            user = Optional.ofNullable(attributes.toDTO());
            memberRepository.saveOAuthUser(user.get());
        }
        return user.get();
    }

}
