package hello.blogService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/authError")
public class AccessDeniedHandlerController {

    @GetMapping()
    @ResponseBody
    public ResponseEntity<Object> responseAuthError(@RequestParam String when){
        // ajax 를 통해서 온 request 일 경우, statusCode 300 전달.
        if (when.equals("ajax")){
        return ResponseEntity.status(300).build();
        }
        // 아닐 경우, ExceptionAdvice 에 정의된 방법으로 IllegalStateException 처리.
        throw new IllegalStateException("접근되지 않는 사용자입니다.");
    }


}
