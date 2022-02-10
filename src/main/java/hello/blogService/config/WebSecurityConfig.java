package hello.blogService.config;

import hello.blogService.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;

    public WebSecurityConfig(MemberService memberService) {
        this.memberService = memberService;
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        // config 객체 내부에 PasswordEncoder 의 구현체로 BcryptPasswordEncoder 를 사용하였으며
        // 이를 스프링 프레임워크에서 사용할 수 있도록 스프링 빈으로 등록
        // 이후 PasswordEncoder 객체를 @AutoWired 를 하여 사용
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable();
        http.authorizeRequests()
                .antMatchers("/","/members/login","/boards/**","/file/**","/img/**","/css/**","members/join","/ckeditor/**","/js/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable()
                .logout()
                .logoutUrl("members/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .and()
                .oauth2Login()
                .loginPage("/googleLogin")
                .userInfoEndpoint()
                .userService(myOAuth2UserService);
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(getPasswordEncoder());
    }
}
