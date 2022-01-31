package hello.blogService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
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
                .disable(); // csrf 토큰 검사 비활성화
//                .authorizeRequests()
//                .antMatchers("/members/login","/members/join","/","/boards")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/members/login") //form 을 통한 로그인 서비스 처리
//                .loginProcessingUrl("/members/login")
//                .usernameParameter("id")
//                .passwordParameter("pwd")
//                .successHandler(new MyLoginSuccessHandler());
    }
}
