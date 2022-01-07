package hello.itemService.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter @Setter
public class Reply {
    private int replyId;
    private String replyContent;
    private String replyWriter;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
    private LocalDateTime replyDate;

    private int boardId;
}
