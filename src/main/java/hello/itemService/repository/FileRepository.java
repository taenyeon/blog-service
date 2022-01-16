package hello.itemService.repository;

import hello.itemService.domain.File;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileRepository {
    List<File> findAll();
    List<File> findByBoardId(@Param("id") String id);
    int insertFiles(@Param("list") List<File> files);
    int deleteFiles(@Param("id") String id);
}
