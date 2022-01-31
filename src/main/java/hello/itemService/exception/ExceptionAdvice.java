package hello.itemService.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler(IllegalStateException.class)
    public String IllegalStateException(IllegalStateException e,
                                        HttpServletResponse response) throws IOException {
        log.info("오류 : {}", e.getMessage());
        response.setContentType("text/html; charset=euc-kr");
        return "<script>alert('" + e.getMessage() + "'); history.go(-1); </script>";

    }
}
