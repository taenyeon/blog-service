package hello.blogService.controller;

import hello.blogService.domain.Blog;
import hello.blogService.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
        model.addAttribute("blog",blog.get());
        return "/blogs/blog";
    }

    @GetMapping("/add")
    public String addBlogForm(HttpServletRequest request){
        String login = (String) request.getSession().getAttribute("login");
        if (login == null) throw new IllegalStateException("로그인 후 이용해주세요.");
        return "/blogs/addBlog";
    }

    @PostMapping("/add")
    public String addBlog(Blog blog,HttpServletRequest request){
        String login = (String) request.getSession().getAttribute("login");
        blog.setBlogWriter(login);
        int result = blogService.insertBlog(blog);
        if(result != 0){
            return "/index";
        } else {
          throw new IllegalStateException("실패");
        }
    }

}
