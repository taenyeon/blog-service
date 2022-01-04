package hello.itemservice.repository;

import hello.itemservice.domain.Files;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileRepository {
    List<Files> findAll();
    List<Files> findById(@Param("id") String id);
    int insertFiles(@Param("list") List<Files> files);
    int deleteFiles(@Param("id") String id);
}
