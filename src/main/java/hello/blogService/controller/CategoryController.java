package hello.blogService.controller;

import hello.blogService.dto.Category;
import hello.blogService.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @RequestMapping("")
    public ResponseEntity<Object> getCategories(){
        List<Category> categories = categoryService.findAll();
        if (!categories.isEmpty()){
        return ResponseEntity.ok(categories);
        } else {
            return ResponseEntity.status(204).build();
        }
    }
    @PostMapping("")
    public ResponseEntity<Object> addCategory(Category category){
        int result = categoryService.insertCategory(category);
        if (result != 0){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(500).build();
        }
    }
}
