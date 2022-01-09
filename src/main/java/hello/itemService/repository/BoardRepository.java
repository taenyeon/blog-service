package hello.itemService.repository;

import hello.itemService.domain.Board;
import hello.itemService.domain.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardRepository {
    List<Board> findByNum(Pagination pagination);
    Optional<Board> findById(@Param("id") String id);
    int insertBoard(Board board);
    int updateBoard(Board board);
    int deleteBoard(@Param("id") String id);
    int hitUp(@Param("id") String id);
    int getBoardListCnt();
}
