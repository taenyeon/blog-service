package hello.blogService.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// AccessDeniedHandler 의 구현체.
@Configuration
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // x-requested-with 을 확인.
        String header = request.getHeader("x-requested-with");
        System.out.println(header);
        // ajax 를 통해 전달된 request 의 경우, header 에 x-requested-with :  XMLHttpRequest 이 포함되어 전달됨.
        // 아닐 경우, null
        if("XMLHttpRequest".equals(header)){
            // ajax 를 통해 전달된 request 의 경우에는 restController 로 responseEntity 를 전달.
            response.sendRedirect("/authError?when=ajax");
        }else {
            // 아닐 경우, loginForm.html 을 전달.
            response.sendRedirect("/authError?when=default");
        }
    }
}
