package hello.itemService.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter @Setter
public class Reply {
    private int replyId;
    private String replyContent;
    private String replyWriter;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate replyDate;
    private int boardId;
}
