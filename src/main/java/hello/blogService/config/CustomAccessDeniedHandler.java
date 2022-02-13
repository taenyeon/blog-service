package hello.blogService.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String header = request.getHeader("x-requested-with");
        System.out.println(header);
        if("XMLHttpRequest".equals(header)){
            response.sendRedirect("/authError?when=ajax");
        }else {
            response.sendRedirect("/authError?when=default");
        }
    }
}
