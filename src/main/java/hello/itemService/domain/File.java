package hello.itemService.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class File {
    private String filePath;
    private String fileName;
    private long fileSize;
    private int boardId;
}
