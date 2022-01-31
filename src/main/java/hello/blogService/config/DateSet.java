package hello.blogService.config;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateSet {
    public static LocalDateTime getNow(){
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}
