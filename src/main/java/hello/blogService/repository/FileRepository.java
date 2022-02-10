package hello.blogService.repository;

import hello.blogService.dto.FileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileRepository {
    List<FileInfo> findAll();
    List<FileInfo> findByBoardId(@Param("id") String id);
    int insertFiles(@Param("list") List<FileInfo> fileInfos);
    int deleteFiles(@Param("id") String id);
}
