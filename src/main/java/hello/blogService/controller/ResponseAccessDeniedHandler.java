package hello.blogService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/authError")
public class ResponseAccessDeniedHandler {

    @GetMapping()
    @ResponseBody
    public ResponseEntity<Object> responseAuthError(@RequestParam String when){
        if (when.equals("ajax")){
        return ResponseEntity.status(300).build();
        }
        throw new IllegalStateException("접근되지 않는 사용자입니다.");
    }


}
