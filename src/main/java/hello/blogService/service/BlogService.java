package hello.blogService.service;

import hello.blogService.domain.Blog;
import hello.blogService.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(rollbackFor = Exception.class)
@Service
public class BlogService {
    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public List<Blog> findAll() {
        return blogRepository.findAll();
    }

    public Optional<Blog> findByBlogAdress(String blogAdress){
        return blogRepository.findByBlogAdress(blogAdress);
    }

    public int insertBlog(Blog blog){
        return blogRepository.insertBlog(blog);
    }

    public List<Blog> findByBlogWriter(String login){
        return blogRepository.findAllByBlogWriter(login);
    }

}
