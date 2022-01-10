package hello.itemService.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(IllegalStateException.class)
    public String IllegalStateException(IllegalStateException e,
                                        HttpServletResponse response,
                                        HttpServletRequest request) throws IOException {
        response.setContentType("text/html; charset=euc-kr");
        return "<script>alert('"+e.getMessage()+"'); history.go(-1); </script>";

    }
}
