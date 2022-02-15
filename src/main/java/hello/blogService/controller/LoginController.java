package hello.blogService.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LoginController {


    @GetMapping("/login")
    public String getGoogleForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // x-requested-with 을 확인.
        String header = request.getHeader("x-requested-with");
        System.out.println(header);
        // ajax 를 통해 전달된 request 의 경우, header 에 x-requested-with :  XMLHttpRequest 이 포함되어 전달됨.
        // 아닐 경우, null
        if("XMLHttpRequest".equals(header)) {
            // ajax 를 통해 전달된 request 의 경우에는 restController 로 responseEntity 를 전달.
            response.sendRedirect("/authError?when=ajax");
        }
        return "members/loginMemberForm";
    }
}
