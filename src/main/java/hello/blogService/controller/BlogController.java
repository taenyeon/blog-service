package hello.blogService.controller;

import hello.blogService.domain.Blog;
import hello.blogService.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/blogs")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping()
    public String blogs(Model model){
        List<Blog> blogs = blogService.findAll();
        model.addAttribute("blogs",blogs);
        return "";
    }

    @GetMapping("/{blogAdress}")
    public String blog(@PathVariable String blogAdress,Model model){
        Optional<Blog> blog = blogService.findByBlogAdress(blogAdress);
        model.addAttribute("blog",blog);
        return "";
    }

    @GetMapping("/add")
    public String addBlogForm(){

    }

}
