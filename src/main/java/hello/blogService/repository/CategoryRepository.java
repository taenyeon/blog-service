package hello.blogService.repository;

import hello.blogService.dto.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryRepository {
    List<Category> findAll();
    int insertCategory(Category category);
}
