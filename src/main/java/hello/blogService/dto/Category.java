package hello.blogService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Category {

    private int categoryId;

    private String categoryName;

    private boolean categoryIsDel;

}
