package hello.itemservice.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Files {
    private String filePath;
    private String fileName;
    private long fileSize;
    private int boardId;
}
