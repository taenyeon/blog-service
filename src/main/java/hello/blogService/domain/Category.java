package hello.blogService.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Category {

    private int categoryId;

    private String categoryName;

    private String blogAdress;

    private boolean categoryIsDel;

}
