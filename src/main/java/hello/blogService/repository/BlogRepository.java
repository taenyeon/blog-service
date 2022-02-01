package hello.blogService.repository;

import hello.blogService.domain.Blog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BlogRepository {
    List<Blog> findAll();
    List<Blog> findAllByBlogWriter(String login);
    Optional<Blog> findByBlogAdress(String blogAdress);
    int insertBlog(Blog blog);
    int updateBlog(Blog blog);
    int deleteBlog(String blogAdress);
}
