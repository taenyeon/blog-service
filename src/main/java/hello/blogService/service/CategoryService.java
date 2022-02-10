package hello.blogService.service;

import hello.blogService.dto.Category;
import hello.blogService.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public int insertCategory(Category category){
        return categoryRepository.insertCategory(category);
    }
}
