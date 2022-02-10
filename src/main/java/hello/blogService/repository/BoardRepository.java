package hello.blogService.repository;

import hello.blogService.dto.Board;
import hello.blogService.dto.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardRepository {

    List<Board> findByNum(Pagination pagination);
    /* page 정보(Pagination)를 이용하여 해당 page 에 보여줄 게시물들을 가져온다.
      예로 1번 페이지라면 10개의 게시물을 startList ~ endList 까지 -> 1~9까지의 게시물의 정보(Board)를 List 에 담아서 보낸다. */

    Optional<Board> findById(@Param("boardId") String boardId);
    /*  게시물 번호(boardId)를 이용하여 해당 게시물의 정보(Board)를 가져온다.
      이때, 없는 게시물 번호일 경우, NullPointException 이 발생할 수 있어 Optional 로 묶어서 보냈다.
    */
    int insertBoard(Board board);
    /* 게시물의 정보(Board)를 이용하여

    */
    int updateBoard(Board board);

    int deleteBoard(@Param("boardId") String boardId);

    int hitUp(@Param("boardId") String boardId);

    int getBoardListCnt();
}
