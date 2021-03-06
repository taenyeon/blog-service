package hello.blogService.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class Board {
    private String boardId;

    private String categoryId;

    private String boardTitle;

    private String boardContent;

    private String boardWriter;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate boardWriteDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate boardModifyDate;

    private boolean boardIsDel;

    private int boardHit;

    private String boardMainImg;

    private boolean boardIsPrivate;

    private List<FileInfo> fileInfos;
}
