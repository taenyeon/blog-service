package hello.blogService.repository;

import hello.blogService.domain.Board;
import hello.blogService.domain.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardRepository {
    List<Board> findByNum(Pagination pagination);
    Optional<Board> findById(@Param("boardId") String boardId);
    int insertBoard(Board board);
    int updateBoard(Board board);
    int deleteBoard(@Param("boardId") String boardId);
    int hitUp(@Param("boardId") String boardId);
    int getBoardListCnt();
}
