package hello.blogService.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class FileInfo {

    private String filePath;

    private String fileName;

    private long fileSize;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fileDate;

    private int boardId;
}
