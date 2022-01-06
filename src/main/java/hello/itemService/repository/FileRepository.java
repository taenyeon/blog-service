package hello.itemService.repository;

import hello.itemService.domain.Files;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileRepository {
    List<Files> findAll();
    List<Files> findByBoardId(@Param("id") String id);
    int insertFiles(@Param("list") List<Files> files);
    int deleteFiles(@Param("id") String id);
}
