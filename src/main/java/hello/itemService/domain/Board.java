package hello.itemService.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class Board {
    private int id;

    private int num;

    private String title;

    private String content;

    private String writer;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private int hit;

    private List<File> files;
    private List<Reply> replies;
}
